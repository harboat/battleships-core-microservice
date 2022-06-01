package com.github.harboat.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
class PasswordEncoderProvider {
    @Bean
    PasswordEncoder getInstance() {
        return new BCryptPasswordEncoder(8, new SecureRandom());
    }
}
