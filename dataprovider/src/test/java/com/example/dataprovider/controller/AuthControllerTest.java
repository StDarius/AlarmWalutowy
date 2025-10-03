package com.example.dataprovider.controller;

import com.example.dataprovider.api.AuthController;
import com.example.dataprovider.model.User;
import com.example.dataprovider.security.JwtService;
import com.example.dataprovider.service.AuthService;
import com.example.dataprovider.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // Controller dependencies
    @MockBean UserService userService;
    @MockBean JwtService jwtService;
    @MockBean AuthService authService;

    @Test
    void shouldRegisterUser() throws Exception {
        // Arrange
        var json = """
        {
          "username": "john",
          "password": "Secret123!",
          "email": "john@example.com"
        }
        """;

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("john");
        mockUser.setEmail("john@example.com");
        mockUser.setPasswordHash("hashed");

        when(userService.register("john", "Secret123!", "john@example.com"))
                .thenReturn(mockUser);
        when(jwtService.generateToken(anyString()))
                .thenReturn("fake.jwt.token");


        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())                 // AuthController returns 200 OK
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andExpect(jsonPath("$.user.username").value("john"))
                .andExpect(jsonPath("$.user.email").value("john@example.com"));
    }
}
