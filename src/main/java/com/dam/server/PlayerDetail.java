package com.dam.server;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PlayerDetail implements UserDetails {
    private final Player player;

    public PlayerDetail(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String getEmail() {
        return player.getEmail();
    }

    // 實作介面規範的方法
    public String getUsername() {
        return player.getUsername();
    }

    public String getPassword() {
        return player.getPassword();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }
}
