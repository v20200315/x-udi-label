package com.lsxuan.udilabel.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/auth/**", "/error").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico").permitAll()
                .requestMatchers("/api/v1/ai/**").permitAll().requestMatchers("/console/**").permitAll()
                .requestMatchers("/api/v1/**").permitAll().anyRequest().permitAll());

        return http.build();
    }

}


