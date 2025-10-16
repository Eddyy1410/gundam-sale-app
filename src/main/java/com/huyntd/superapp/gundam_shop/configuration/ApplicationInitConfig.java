package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    ProductRepository productRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if(userRepository.findByEmail("admin@huyntd.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("admin@huyntd.com")
                        .role(UserRole.ADMIN)
                        .passwordHash(passwordEncoder.encode("admin"))
                        .build()
                );
                log.warn("admin@huyntd.com has been created with default password: admin, please change it!");
            }
            if (productRepository.findAll().isEmpty()) {
                log.warn(productRepository.findAll().toString());
            }
        };
    }


}
