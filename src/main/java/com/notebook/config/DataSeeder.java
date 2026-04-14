package com.notebook.config;

import com.notebook.entity.Role;
import com.notebook.entity.User;
import com.notebook.repository.RoleRepository;
import com.notebook.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Roles
            Role roleUser = roleRepository.findByName("ROLE_USER").orElse(null);
            if (roleUser == null) {
                roleUser = new Role();
                roleUser.setName("ROLE_USER");
                roleRepository.save(roleUser);
            }

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            if (roleAdmin == null) {
                roleAdmin = new Role();
                roleAdmin.setName("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }

            // Seed Admin User
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFullName("System Administrator");
                admin.setEmail("admin@notebook.local");
                
                ArrayList<Role> roles = new ArrayList<>();
                roles.add(roleAdmin);
                admin.setRoles(roles);
                
                userRepository.save(admin);
            }
        };
    }
}
