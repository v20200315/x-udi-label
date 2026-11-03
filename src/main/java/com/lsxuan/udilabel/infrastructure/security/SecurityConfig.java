package com.lsxuan.udilabel.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        ClientRegistrationRepository clientRegistrationRepository
    ) throws Exception {

        // OIDC Logout Handler
        OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler =
            new OidcClientInitiatedLogoutSuccessHandler(
                clientRegistrationRepository
            );

        // Keycloak 注销成功后，返回应用登录入口
        logoutSuccessHandler.setPostLogoutRedirectUri(
            "{baseUrl}/logged-out"
        );

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/access-denied",
                    "/logged-out",
                    "/error",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/fonts/**",
                    "/favicon.ico"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/console", true)
            )

            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(
                    new LoginUrlAuthenticationEntryPoint(
                        "/oauth2/authorization/keycloak"
                    )
                )
                .accessDeniedPage("/access-denied")
            )

            .sessionManagement(session -> session
                .sessionFixation(
                    SessionManagementConfigurer.SessionFixationConfigurer::migrateSession
                )
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )

            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            );

        return http.build();
    }
}