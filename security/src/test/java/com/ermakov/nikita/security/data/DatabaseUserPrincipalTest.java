package com.ermakov.nikita.security.data;

import com.ermakov.nikita.model.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseUserPrincipalTest {

    private DatabaseUserPrincipal databaseUserPrincipal;

    @BeforeEach
    void before() {
        final User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        databaseUserPrincipal = new DatabaseUserPrincipal(user);
    }

    @Test
    void userPrincipalShouldReturnUserName() {
        assertEquals("username", databaseUserPrincipal.getUsername());
    }

    @Test
    void userPrincipalShouldReturnUserPassword() {
        assertEquals("password", databaseUserPrincipal.getPassword());
    }

    @Test
    void userPrincipalShouldReturnUserNonExpired() {
        assertTrue(databaseUserPrincipal.isAccountNonExpired());
    }

    @Test
    void userPrincipalShouldReturnUserNonLocked() {
        assertTrue(databaseUserPrincipal.isAccountNonLocked());
    }

    @Test
    void userPrincipalShouldReturnUserCredentialsNonExpired() {
        assertTrue(databaseUserPrincipal.isCredentialsNonExpired());
    }

    @Test
    void userPrincipalShouldReturnUserIsEnabled() {
        assertTrue(databaseUserPrincipal.isEnabled());
    }
}
