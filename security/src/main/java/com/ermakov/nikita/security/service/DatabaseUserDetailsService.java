package com.ermakov.nikita.security.service;

import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.repository.UserRepository;
import com.ermakov.nikita.security.data.DatabaseUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public DatabaseUserDetailsService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Not found user %s", username));
        }
        return new DatabaseUserPrincipal(user);
    }
}
