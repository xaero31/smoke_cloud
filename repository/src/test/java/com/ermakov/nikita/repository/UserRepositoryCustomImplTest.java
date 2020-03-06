package com.ermakov.nikita.repository;

import com.ermakov.nikita.entity.security.Role;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.repository.custom.UserRepositoryCustomImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryCustomImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    @SuppressWarnings("rawtypes")
    private TypedQuery typedQuery;

    private UserRepositoryCustomImpl userRepository;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void before() {
        userRepository = new UserRepositoryCustomImpl(entityManager);

        when(entityManager.createQuery(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyInt(), any())).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), anyBoolean())).thenReturn(typedQuery);
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

    @Test
    void saveNewUserTest() {
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        userRepository.saveUniqueUser(new User());
        verify(entityManager).persist(any(User.class));
    }

    @Test
    void whenSavingExistingUserRepositoryShouldThrowAnException() {
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);
        doThrow(EntityExistsException.class).when(entityManager).persist(any(User.class));

        assertThrows(EntityExistsException.class, () -> userRepository.saveUniqueUser(new User()));
    }

}