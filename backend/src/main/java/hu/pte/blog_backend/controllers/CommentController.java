package hu.pte.blog_backend.controllers;

import hu.pte.blog_backend.models.dtos.CommentRequestDTO;
import hu.pte.blog_backend.models.dtos.CommentResponseDTO;
import hu.pte.blog_backend.services.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts/comments/")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public CommentRequestDTO addCommentToPost(@PathVariable UUID id, @Valid @RequestBody CommentRequestDTO comment, @RequestHeader("Authorization") String token){
        return commentService.addCommentToPost(id, comment, token.substring(7));
    }

    @GetMapping("/{id}")
    public CommentResponseDTO getCommentById(@PathVariable UUID id){
        return commentService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable UUID id, @RequestHeader("Authorization") String token){
        commentService.deleteCommendById(id, token.substring(7));
    }

}
