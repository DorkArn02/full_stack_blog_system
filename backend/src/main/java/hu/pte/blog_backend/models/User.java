package hu.pte.blog_backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@Component
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID user_id;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8)
    private String password;

    @Column(unique = true, nullable = false)
    @Email
    @NotBlank
    private String email;
/*
    @OneToMany(mappedBy = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Post> posts;
*/
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id") ,
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    @NotBlank
    private String picture;

    @Column(nullable = false)
    private boolean active;
}
