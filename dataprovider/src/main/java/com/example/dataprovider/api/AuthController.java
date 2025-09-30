
package com.example.dataprovider.api;

import com.example.dataprovider.mapper.Mappers;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.UserRepository;
import com.example.dataprovider.security.JwtService;
import com.example.dataprovider.service.AuthService;
import com.example.dataprovider.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import com.example.dataprovider.dto.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepo;


    public static class RegisterRequest {
        @NotBlank public String username;
        @NotBlank public String password;
        @Email public String email;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequest req) {
        var u = userService.register(req.username, req.password, req.email);
        var dto = new AuthResponseDTO(
                jwtService.generateToken(u.getUsername()),
                Mappers.toDTO(u)
        );
        return ResponseEntity.ok(dto);
    }

    public static class LoginRequest {
        @NotBlank public String username;
        @NotBlank public String password;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO req) {
        var dto = authService.login(req.username(), req.password());
        return ResponseEntity.ok(dto);
    }

}
