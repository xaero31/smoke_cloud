package com.ermakov.nikita.security.service;

import com.ermakov.nikita.model.security.User;
import com.ermakov.nikita.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DatabaseUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private DatabaseUserDetailsService detailsService;

    @BeforeEach
    void before() {
        detailsService = new DatabaseUserDetailsService(userRepository);
    }

    @Test
    void onNullUserServiceShouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(anyString()));
    }

    @Test
    void onFoundedUserServiceShouldReturnThisUser() {
        final User user = new User();
        user.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        assertEquals(user.getPassword(), detailsService.loadUserByUsername(anyString()).getPassword());
    }
}
