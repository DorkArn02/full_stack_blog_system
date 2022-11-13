package hu.pte.blog_backend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.pte.blog_backend.models.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID user_id;
    private String title;
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;


    public PostRequestDTO(Post post){
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user_id = post.getUser_id().getUser_id();
    }
}
