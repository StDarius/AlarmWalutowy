
package com.example.dataprovider.service;

import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void registerSuccess() {
        UserRepository repo = mock(UserRepository.class);
        when(repo.existsByUsername("john")).thenReturn(false);
        when(repo.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        PasswordEncoder enc = mock(PasswordEncoder.class);
        when(enc.encode("pw")).thenReturn("HASH");

        UserService svc = new UserService(repo, enc);
        User u = svc.register("john", "pw", "john@example.com");

        assertEquals("john", u.getUsername());
        assertEquals("HASH", u.getPasswordHash());
        assertEquals("john@example.com", u.getEmail());
    }
}
