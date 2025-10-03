package com.example.dataprovider.api;

import com.example.dataprovider.dto.AuthRequestDTO;
import com.example.dataprovider.dto.AuthResponseDTO;
import com.example.dataprovider.mapper.Mappers;
import com.example.dataprovider.security.JwtService;
import com.example.dataprovider.service.AuthService;
import com.example.dataprovider.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;

    public static record RegisterRequest(
            @NotBlank String username,
            @NotBlank String password,
            @Email String email
    ) {}

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequest req) {
        var user = userService.register(req.username(), req.password(), req.email());

        var body = new AuthResponseDTO(
                jwtService.generateToken(user.getUsername()),
                Mappers.toDTO(user)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO req) {
        var body = authService.login(req.username(), req.password());
        return ResponseEntity.ok(body);
    }
}
