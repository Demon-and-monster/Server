package com.dam.server;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

class InMemoryUserDetailsManager implements UserDetailsService {
    private final Map<String, PlayerDetail> users = new HashMap<>();

    public InMemoryUserDetailsManager() {
    }

    public void addPlayer(Player player) {
        users.put(player.getUsername(), new PlayerDetail(player));
    }

    public Player getPlayerByUsername(String username) {
        PlayerDetail player = users.get(username.toLowerCase());
        if (player == null) {
            throw new UsernameNotFoundException(username);
        }else{
            return player.getPlayer();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
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
