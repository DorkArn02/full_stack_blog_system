package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Role;
import hu.pte.blog_backend.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private UUID user_id;
    private String email;
    private Set<Role> roles;

    public UserResponseDTO(User user){
        this.user_id = user.getUser_id();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
