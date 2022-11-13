package hu.pte.blog_backend.services;

import hu.pte.blog_backend.models.Role;
import hu.pte.blog_backend.models.User;
import hu.pte.blog_backend.models.dtos.AuthRequestDTO;
import hu.pte.blog_backend.models.dtos.AuthResponseDTO;
import hu.pte.blog_backend.models.dtos.RegisterRequestDTO;
import hu.pte.blog_backend.repository.RoleRepository;
import hu.pte.blog_backend.repository.UserRepository;
import hu.pte.blog_backend.security.CustomUserDetails;
import hu.pte.blog_backend.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    public RegisterRequestDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()
                || userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPicture("default.png");
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequestDTO.getPassword()));
        user.setActive(true);
        user.setEmail(registerRequestDTO.getEmail());
        if(roleRepository.findByName("ROLE_USER").isEmpty()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Role userR = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Set.of(userR));
        return new RegisterRequestDTO(userRepository.save(user));
    }
    public ResponseEntity<AuthResponseDTO> loginUser(AuthRequestDTO request){
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );
            var user = (CustomUserDetails) authenticate.getPrincipal();
            AuthResponseDTO authResponseDTO = AuthResponseDTO
                    .builder().username(user.getUsername())
                    .email(user.getEmail()).user_id(user.getUser_id())
                    .roles(user.getRoles()).build();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(user)
                    )
                    .body(authResponseDTO);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
