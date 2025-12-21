package com.ex.expensetracker.Config;

import com.ex.expensetracker.Repository.UserRepository;
import com.ex.expensetracker.model.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfiguration {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));  // BCrypted password

                // If AppUser has other fields, set them here:
                // admin.setEmail("admin@gmail.com");

                userRepository.save(admin);

                System.out.println("✔ Admin user created: admin / admin123");
            }
        };
    }
}
