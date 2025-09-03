package com.example.lms;

import com.example.lms.entity.ERole;
import com.example.lms.entity.Role;
import com.example.lms.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class LmsBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsBackEndApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
            // Check if roles exist, if not create them
            if (roleRepository.findByName(ERole.ROLE_STUDENT).isEmpty()) {
                roleRepository.save(new Role(null, ERole.ROLE_STUDENT));
            }
            if (roleRepository.findByName(ERole.ROLE_INSTRUCTOR).isEmpty()) {
                roleRepository.save(new Role(null, ERole.ROLE_INSTRUCTOR));
            }
            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(null, ERole.ROLE_ADMIN));
            }

           
        };
    }
}