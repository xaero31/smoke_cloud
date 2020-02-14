package com.ermakov.nikita.security.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * created by Nikita_Ermakov at 2/14/2020
 */
@ExtendWith(MockitoExtension.class)
public class SecurityTest {

    private SecurityConfig config;

    @BeforeEach
    void before() {
        config = new SecurityConfig();
    }

    @Test
    void securityConfigShouldHaveUserUser() {
        assertNotNull(config.userDetailsService(), "UserDetailsService is null");
    }
}
