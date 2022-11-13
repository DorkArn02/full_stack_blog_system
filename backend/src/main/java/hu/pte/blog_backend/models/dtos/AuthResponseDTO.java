package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    private UUID user_id;
    private String username;
    private String email;
    private Set<Role> roles;
}
