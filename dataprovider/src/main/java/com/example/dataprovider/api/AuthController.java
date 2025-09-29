
package com.example.dataprovider.api;

import com.example.dataprovider.model.User;
import com.example.dataprovider.security.JwtService;
import com.example.dataprovider.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtService jwtService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public static class RegisterRequest {
        @NotBlank public String username;
        @NotBlank public String password;
        @Email public String email;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User u = userService.register(req.username, req.password, req.email);
        String token = jwtService.generateToken(u.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    public static class LoginRequest {
        @NotBlank public String username;
        @NotBlank public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        String token = jwtService.generateToken(req.username);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
