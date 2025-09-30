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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;

    // Request payload for registration
    public static record RegisterRequest(
            @NotBlank String username,
            @NotBlank String password,
            @Email String email
    ) {}

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequest req) {
        var u = userService.register(req.username(), req.password(), req.email());
        var dto = new AuthResponseDTO(
                jwtService.generateToken(u.getUsername()),
                Mappers.toDTO(u)
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO req) {
        var dto = authService.login(req.username(), req.password());
        return ResponseEntity.ok(dto);
    }
}
