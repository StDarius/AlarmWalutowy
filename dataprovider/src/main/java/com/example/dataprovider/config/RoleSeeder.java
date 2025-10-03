package com.example.dataprovider.config;

import com.example.dataprovider.model.Role;
import com.example.dataprovider.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seed("ROLE_USER");
        seed("ROLE_ADMIN");
    }

    private void seed(String name) {
        if (!roleRepository.existsByName(name)) {
            Role r = new Role();
            r.setName(name);
            roleRepository.save(r);
        }
    }
}
