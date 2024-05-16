package com.dam.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .build();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.addPlayer(new Player(
                "0000",
                "admin",
                "password",
                "yuhongweng0501@gmail.com",
                1000
        ));

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}

class InMemoryUserDetailsManager implements UserDetailsService {
    private final Map<String, PlayerDetail> users = new HashMap<>();

    public InMemoryUserDetailsManager() {
    }

    public void addPlayer(Player player) {
        users.put(player.getUsername(), new PlayerDetail(player));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            System.out.println(user.getUsername());
            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    user.isAccountNonExpired(),
                    user.isCredentialsNonExpired(),
                    user.isAccountNonLocked(),
                    user.getAuthorities()
            );
        }
    }
}
