package hu.pte.blog_backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "posts")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID post_id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Comment> comments;

    @Type(type = "uuid-char")
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user_id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id") ,
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Column(nullable = false)
    @NotBlank
    private String picture;
}

