package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDTO {
    private UUID comment_id;
    private UUID user_id;
    private UUID post_id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String author;

    public CommentResponseDTO(Comment comment, String author) {
        this.comment_id = comment.getComment_id();
        this.user_id = comment.getUser_id().getUser_id();
        this.post_id = comment.getPost_id().getPost_id();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.author = author;
    }
}
