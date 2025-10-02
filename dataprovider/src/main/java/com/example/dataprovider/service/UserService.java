
package com.example.dataprovider.service;

import com.example.dataprovider.model.User;
import com.example.dataprovider.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.dataprovider.model.Role;
import com.example.dataprovider.repo.RoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository repo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;


    public UserService(UserRepository repo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    public User register(String username, String rawPassword, String email) {
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setEmail(email);

        // przypisz ROLE_USER z DB
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not seeded"));
        u.addRole(userRole);

        return repo.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName())) // nazwa w formacie "ROLE_XYZ"
                .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}