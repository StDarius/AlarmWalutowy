package com.example.dataprovider.service;

import com.example.dataprovider.dto.AuthResponseDTO;
import com.example.dataprovider.mapper.Mappers;
import com.example.dataprovider.repo.UserRepository;
import com.example.dataprovider.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor // generates constructor for final fields
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(String username, String rawPassword) {
        var user = userRepo.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials")
        );

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        var token = jwtService.generateToken(user.getUsername());
        return new AuthResponseDTO(token, Mappers.toDTO(user));
    }
}