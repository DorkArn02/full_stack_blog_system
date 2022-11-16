package hu.pte.blog_backend.controllers;

import hu.pte.blog_backend.models.User;
import hu.pte.blog_backend.models.dtos.UserRequestDTO;
import hu.pte.blog_backend.models.dtos.UserResponseDTO;
import hu.pte.blog_backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{uuid}")
    public void deleteUser(@PathVariable UUID uuid, @RequestHeader("Authorization") String token) {
        userService.deleteUser(uuid, token.substring(7));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(uuid));
    }

    @PostMapping(value = "/profile/{uuid}") // , consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    public void uploadProfilePicture(@PathVariable UUID uuid, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException{
        userService.uploadProfilePicture(uuid, file, token.substring(7));
    }

    @GetMapping(value = "/profile/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> getImage(@PathVariable UUID uuid) throws IOException{
        return userService.getImage(uuid);
    }

    @PutMapping("/{uuid}")
    public void updateUser(@PathVariable UUID uuid, @RequestBody UserRequestDTO user, @RequestHeader("Authorization") String token){
        userService.updateUser(uuid, user, token.substring(7));
    }
}
