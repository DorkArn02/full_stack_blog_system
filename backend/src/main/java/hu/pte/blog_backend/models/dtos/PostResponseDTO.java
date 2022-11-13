package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDTO {

    private UUID post_id;
    private UUID user_id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String author;
    private List<CommentResponseDTO> comments;

    public PostResponseDTO(Post post, String author){
        this.post_id = post.getPost_id();
        this.user_id = post.getUser_id().getUser_id();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.author = author;

        List<CommentResponseDTO> commentDTOS = new ArrayList<>();

        post.getComments().forEach(comment -> {
             commentDTOS.add(new CommentResponseDTO(comment, author));
        });

        this.comments = commentDTOS;

    }
}
