package com.example.dataprovider.service;

import com.example.dataprovider.model.Role;
import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.RoleRepository;
import com.example.dataprovider.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserService userService;

    @Test
    void registerAssignsUserRole() {
        // arrange
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("HASH");

        var role = new Role();
        role.setName("ROLE_USER"); // <-- with prefix

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // act
        var u = userService.register("alice", "pw", "a@b.c");

        // assert
        assertThat(u.getPasswordHash()).isEqualTo("HASH");
        assertThat(u.getRoles())
                .extracting(Role::getName)
                .contains("ROLE_USER"); // <-- expect prefixed name
    }
}