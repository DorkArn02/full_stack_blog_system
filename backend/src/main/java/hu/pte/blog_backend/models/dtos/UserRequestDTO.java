package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;

    public UserRequestDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
