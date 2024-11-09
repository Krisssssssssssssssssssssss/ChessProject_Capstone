package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.url}")
    private String appUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/secured").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Use session for OAuth2
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Unauthorized handler
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl(appUrl) // Redirect to app URL on OAuth2 success
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl(appUrl) // Redirect to app URL on logout
                );
        return http.build();
    }
}
