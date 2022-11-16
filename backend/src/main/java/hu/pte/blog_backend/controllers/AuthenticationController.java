package hu.pte.blog_backend.controllers;

import hu.pte.blog_backend.models.dtos.AuthRequestDTO;
import hu.pte.blog_backend.models.dtos.AuthResponseDTO;
import hu.pte.blog_backend.models.dtos.RegisterRequestDTO;
import hu.pte.blog_backend.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRequestDTO> registerUser(@Valid @RequestBody RegisterRequestDTO user) {
        return new ResponseEntity<>(authenticationService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return authenticationService.loginUser(request);
    }

}
