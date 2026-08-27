package com.pointwest.bootcamp.eventhubri.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.identity.repository.UserRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedTestUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String email = "romille.ilaida@pointwest.com";
            String password = "Password123!";

            if (userRepository.findByEmailIgnoreCase(email).isEmpty()) {

                User user = new User();

                user.setEmail(email);
                user.setPasswordHash(
                        passwordEncoder.encode(password)
                );
                user.setFirstName("Test");
                user.setLastName("User");
                user.setPhoneNumber("09171234567");
                user.setIsActive(true);

                userRepository.save(user);

                System.out.println(
                    "=========================================="
                );
                System.out.println(
                    "Test user created"
                );
                System.out.println(
                    "Email: " + email
                );
                System.out.println(
                    "Password: " + password
                );
                System.out.println(
                    "=========================================="
                );
            }
        };
    }
}
