package hu.pte.blog_backend.services;

import hu.pte.blog_backend.models.Post;
import hu.pte.blog_backend.models.User;
import hu.pte.blog_backend.models.dtos.PostRequestDTO;
import hu.pte.blog_backend.models.dtos.PostResponseDTO;
import hu.pte.blog_backend.repository.CategoryRepository;
import hu.pte.blog_backend.repository.PostRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CategoryRepository categoryRepository;
    @Value("${images_location}")
    private String FILE_PATH;
    public PostService(PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all posts from database
     * @return List<PostDTO>
     */
    public List<PostResponseDTO> getAllPosts() {
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();

        postRepository.findAll().forEach(post -> postResponseDTOS.add(new PostResponseDTO(post, userRepository.findById(post.getUser_id().getUser_id()).get().getUsername())));
        return postResponseDTOS;
    }

    /**
     * Add new post to the database
     * @param post RequestBody Post
     * @param token Authorization Bearer token
     * @return PostDTO
     */
    public PostRequestDTO addNewPost(PostRequestDTO post, String token) {
        if (userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isPresent()) {

            Post postDb = new Post();
            postDb.setContent(post.getContent());
            postDb.setTitle(post.getTitle());
            postDb.setCreatedAt(LocalDateTime.now());
            postDb.setUpdatedAt(LocalDateTime.now());
            postDb.setUser_id(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get());
            postDb.setPicture("defaultPost.png");
            return new PostRequestDTO(postRepository.save(postDb));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a post by id
     * @param uuid Post's id
     * @return PostResponseDTO
     */
    public PostResponseDTO getPostById(UUID uuid){
        if(postRepository.findById(uuid).isPresent()){
            return new PostResponseDTO(postRepository.findById(uuid).get(), userRepository.findById(postRepository.findById(uuid).get().getUser_id().getUser_id()).get().getUsername());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a post by title
     * @param title Post's title
     * @return PostResponseDTO
     */
    public List<PostResponseDTO> getPostByTitle(String title){
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();

        postRepository.findByTitleIgnoreCaseContaining(title).forEach(post -> postResponseDTOS.add(new PostResponseDTO(post, userRepository.findById(post.getUser_id().getUser_id()).get().getUsername())));
        return postResponseDTOS;
    }

    /**
     * Update a post by id
     * @param post PostRequestDTO
     * @param uuid Post's uuid
     * @param token Bearer authorization token
     * @return PostRequestDTO
     */
    public PostRequestDTO updatePostById(PostRequestDTO post, UUID uuid, String token){
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            User user = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get();

            if(!postRepository.findById(uuid).get().getUser_id().getUser_id().equals(user.getUser_id())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            if(postRepository.findById(uuid).isPresent()){
                Post p = postRepository.findById(uuid).get();
                p.setTitle(post.getTitle());
                p.setContent(post.getContent());
                p.setUpdatedAt(LocalDateTime.now());
                return new PostRequestDTO(postRepository.save(p));
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Delete a post by id
     * @param uuid Post's id
     * @param token Bearer authorization token
     */
    public void deletePostById(UUID uuid, String token){
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            User user = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get();

            if(postRepository.findById(uuid).isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            if(!postRepository.findById(uuid).get().getUser_id().getUser_id().equals(user.getUser_id())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            if(postRepository.findById(uuid).isPresent()){
                postRepository.deleteById(uuid);
            }
        }
    }

    /**
     * Assign a category to a post
     * @param postId Post's id
     * @param categoryId Category's id
     * @param token Bearer authorization token
     */
    public void addCategoryToPost(UUID postId, Integer categoryId, String token){
        if(postRepository.findById(postId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(categoryRepository.findById(categoryId).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Post post = postRepository.findById(postId).get();

        if(!post.getUser_id().getUsername().equals(jwtUtil.getUsernameFromToken(token))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        post.getCategories().add(categoryRepository.findById(categoryId).get());

        postRepository.save(post);
    }

    /**
     * Delete a category from a post
     * @param postId Post's id
     * @param categoryId Category's
     * @param token Bearer authorization token
     */
    public void deleteCategoryToPost(UUID postId, Integer categoryId, String token){
        if(postRepository.findById(postId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(categoryRepository.findById(categoryId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Post post = postRepository.findById(postId).get();

        if(!post.getUser_id().getUsername().equals(jwtUtil.getUsernameFromToken(token))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        post.getCategories().remove(categoryRepository.findById(categoryId).get());

        postRepository.save(post);
    }

    /**
     * Upload post picture by id
     * @param uuid Post's id
     * @param file Post picture
     * @param token Bearer authorization token
     * */
    public void uploadPostPicture(UUID uuid, MultipartFile file, String token) throws IOException {
        if(postRepository.findById(uuid).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isPresent()){
            if(!userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get().getUser_id().equals(postRepository.findById(uuid).get().getUser_id().getUser_id()))
            {
               throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            Post post = postRepository.findById(uuid).get();
            String ext = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            post.setPicture(uuid + "." + ext);
            file.transferTo(new File(FILE_PATH + uuid + "." + ext));
            postRepository.save(post);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get post's image
     * @param uuid Post's id
     * @return Image
     */
    public ResponseEntity<?> getImage(UUID uuid) throws IOException {
        if(postRepository.findById(uuid).isPresent()){
            Post post = postRepository.findById(uuid).get();
            byte[] d = Files.readAllBytes(new File(FILE_PATH + post.getPicture()).toPath());
            MediaType m = post.getPicture().split("\\.")[1].equals("jpg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
            return ResponseEntity.status(HttpStatus.OK).contentType(m).body(d);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
