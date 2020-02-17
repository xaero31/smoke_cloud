package com.ermakov.nikita.security.data;

import com.ermakov.nikita.model.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
public class DatabaseUserPrincipal implements UserDetails {

    private User user;

    public DatabaseUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
