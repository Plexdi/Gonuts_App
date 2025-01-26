package com.gonuts.gonutsbackend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity (not recommended for production)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/menu/**").authenticated() // Require authentication for /api/menu/*
                .anyRequest().permitAll() // Allow all other endpoints
            )
            .httpBasic(); // Enable Basic Auth

        return http.build();
    }
}
