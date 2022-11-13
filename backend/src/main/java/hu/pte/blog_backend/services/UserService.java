package hu.pte.blog_backend.services;
import hu.pte.blog_backend.models.User;
import hu.pte.blog_backend.models.dtos.UserRequestDTO;
import hu.pte.blog_backend.models.dtos.UserResponseDTO;
import hu.pte.blog_backend.repository.UserRepository;
import hu.pte.blog_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Value("${images_location}")
    private String FILE_PATH;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get user by id
     * @param uuid User's id
     * @return UserRequestDTO
     */
    public UserResponseDTO getUser(UUID uuid) {
        if(userRepository.findById(uuid).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this id");
        }
        return new UserResponseDTO(userRepository.findById(uuid).get());
    }

    /**
     * Upload profile picture by id
     * @param uuid User's id
     * @param file Profile picture
     * @param token Bearer authorization token
     * */
    public void uploadProfilePicture(UUID uuid, MultipartFile file, String token) throws IOException {
        if(userRepository.findById(uuid).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isPresent()){
            User user = userRepository.findById(uuid).get();
            String ext = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            user.setPicture(uuid + "." + ext);
            file.transferTo(new File(FILE_PATH + uuid + "." + ext));
            userRepository.save(user);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getImage(UUID uuid) throws IOException {
        if(userRepository.findById(uuid).isPresent()){
            User user = userRepository.findById(uuid).get();
            byte[] d = Files.readAllBytes(new File(FILE_PATH + user.getPicture()).toPath());
            MediaType m = user.getPicture().split("\\.")[1].equals("jpg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
            return ResponseEntity.status(HttpStatus.OK).contentType(m).body(d);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete user by id
     * @param uuid User's id
     * @param token Bearer authorization token
     */
    public void deleteUser(UUID uuid, String token) {
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            User user = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get();

            if(user.getUser_id().equals(uuid)){
                userRepository.deleteById(uuid);
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }
    public UserRequestDTO updateUser(UUID uuid, UserRequestDTO newUser, String token){
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            User user = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get();

            if(user.getUser_id().equals(uuid)){
                user.setEmail(newUser.getEmail());
                user.setPassword(newUser.getPassword());
                user.setUsername(newUser.getUsername());
                return new UserRequestDTO(userRepository.save(user));
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }

}
