package hu.pte.blog_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID comment_id;

    @Type(type = "uuid-char")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user_id;
    @ManyToOne
    @JoinColumn(name="post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Post post_id;

    @NotBlank
    @Column(nullable = false)
    private String content;

    @DateTimeFormat
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @DateTimeFormat
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
