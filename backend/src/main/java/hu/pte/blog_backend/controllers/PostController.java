package hu.pte.blog_backend.controllers;

import hu.pte.blog_backend.models.dtos.PostRequestDTO;
import hu.pte.blog_backend.models.dtos.PostResponseDTO;
import hu.pte.blog_backend.services.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsByTitle(@RequestParam String title) {
        return new ResponseEntity<>(postService.getPostByTitle(title), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<PostRequestDTO> addNewPost(@Valid @RequestBody PostRequestDTO post, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(postService.addNewPost(post, token.substring(7)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostRequestDTO> updatePostById(@Valid @RequestBody PostRequestDTO post, @PathVariable UUID id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(postService.updatePostById(post, id, token.substring(7)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable UUID id){
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable UUID id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        postService.deletePostById(id, token.substring(7));
    }
    @PostMapping("/category/{postId}")
    public void addCategoryToPost(@PathVariable UUID postId, @RequestBody Integer categoryId, @RequestHeader("Authorization") String token)
    {
        postService.addCategoryToPost(postId, categoryId, token);
    }
    @DeleteMapping("/category/{postId}")
    public void deleteCategoryToPost(@PathVariable UUID postId, @RequestBody Integer categoryId, @RequestHeader("Authorization") String token)
    {
        postService.deleteCategoryToPost(postId, categoryId, token);
    }

    @PostMapping("/picture/{uuid}")
    public void uploadPostPicture(@PathVariable UUID uuid, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException{
        postService.uploadPostPicture(uuid, file, token.substring(7));
    }
    @GetMapping("/picture/{uuid}")
    public ResponseEntity<?> getImage(@PathVariable UUID uuid) throws IOException{
        return postService.getImage(uuid);
    }
}
