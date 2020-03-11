package com.ermakov.nikita.security.data;

import com.ermakov.nikita.entity.security.Privilege;
import com.ermakov.nikita.entity.security.Role;
import com.ermakov.nikita.entity.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUserPrincipalTest {

    private User user;

    private DatabaseUserPrincipal databaseUserPrincipal;

    @BeforeEach
    void before() {
        user = new User();
        databaseUserPrincipal = new DatabaseUserPrincipal(user);
    }

    @Test
    void userGrantAuthoritiesContainsPrivilegesFromUser() {
        final List<Privilege> privilegeList = new ArrayList<>();
        final Privilege read = new Privilege();
        final Privilege write = new Privilege();

        read.setName("READ");
        write.setName("WRITE");

        privilegeList.add(read);
        privilegeList.add(write);

        final Role role = new Role();
        role.setName("USER");
        role.setPrivileges(privilegeList);

        user.setRoles(Collections.singletonList(role));

        assertNotNull(databaseUserPrincipal.getAuthorities());
        assertEquals(privilegeList.size(), databaseUserPrincipal.getAuthorities().size());

        final List<String> authoritiesFromPrincipal = databaseUserPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        final List<String> exceptedAuthorities = privilegeList.stream()
                .map(Privilege::getName)
                .collect(Collectors.toList());

        assertTrue(exceptedAuthorities.containsAll(authoritiesFromPrincipal));
    }

    @Test
    void userGrantAuthoritiesReturnsEmptyListIfUserRolesAreNull() {
        user.setRoles(null);
        assertTrue(databaseUserPrincipal.getAuthorities().isEmpty());
    }

    @Test
    void userGrantAuthoritiesReturnsEmptyListIfRolesPrivilegesAreNull() {
        final Role role = new Role();
        role.setPrivileges(null);

        user.setRoles(Collections.singletonList(role));

        assertTrue(databaseUserPrincipal.getAuthorities().isEmpty());
    }

    @Test
    void userPrincipalShouldReturnUserName() {
        user.setUsername("username");
        assertEquals("username", databaseUserPrincipal.getUsername());
    }

    @Test
    void userPrincipalShouldReturnUserPassword() {
        user.setPassword("password");
        assertEquals("password", databaseUserPrincipal.getPassword());
    }

    @Test
    void userPrincipalShouldReturnUserNonExpired() {
        user.setNonExpired(true);
        assertTrue(databaseUserPrincipal.isAccountNonExpired());
    }

    @Test
    void userPrincipalShouldReturnUserNonLocked() {
        user.setNonLocked(true);
        assertTrue(databaseUserPrincipal.isAccountNonLocked());
    }

    @Test
    void userPrincipalShouldReturnUserCredentialsNonExpired() {
        user.setCredentialsNonExpired(true);
        assertTrue(databaseUserPrincipal.isCredentialsNonExpired());
    }

    @Test
    void userPrincipalShouldReturnUserIsEnabled() {
        user.setEnabled(true);
        assertTrue(databaseUserPrincipal.isEnabled());
    }
}
