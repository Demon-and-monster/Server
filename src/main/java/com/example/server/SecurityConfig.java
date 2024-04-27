package com.example.server;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Key;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry ->
                registry
                        .requestMatchers(HttpMethod.GET, "/test/all/?*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/test/authed/?*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        .anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public TokenService tokenService(@Value("${security.jwt.key}") String key) {
        System.out.println(key);
        Key theKey = Keys.hmacShaKeyFor(key.getBytes());
        return new TokenService(theKey);
    }
}
