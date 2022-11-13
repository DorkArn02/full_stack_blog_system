package hu.pte.blog_backend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.pte.blog_backend.models.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID user_id;
    private String content;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public CommentRequestDTO(Comment comment){
        this.user_id = comment.getUser_id().getUser_id();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

}
