package com.ermakov.nikita.security.repository;

import com.ermakov.nikita.model.security.Role;
import com.ermakov.nikita.model.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    @SuppressWarnings("rawtypes")
    private TypedQuery typedQuery;

    private UserRepository userRepository;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void before() {
        when(entityManager.createQuery(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyInt(), any())).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), anyBoolean())).thenReturn(typedQuery);
        userRepository = new UserRepository(entityManager);
    }

    @Test
    void whenNotFoundUser_throwsNoResultException() {
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);
        assertNull(userRepository.findByUsername("username"));
    }

    @Test
    void whenFoundUser_returnUserObject() {
        final User user = new User();
        when(typedQuery.getSingleResult()).thenReturn(user);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(new Role()));
        assertSame(user, userRepository.findByUsername("username"));
    }
}
