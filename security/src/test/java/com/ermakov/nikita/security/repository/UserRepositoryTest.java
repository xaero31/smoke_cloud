package com.ermakov.nikita.security.repository;

import com.ermakov.nikita.model.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<User> criteriaQuery;

    @Mock
    private Root<User> userRoot;

    @Mock
    private TypedQuery<User> typedQuery;

    private UserRepository userRepository;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void before() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(userRoot);
        when(userRoot.get(anyString())).thenReturn(null);
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
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
        assertSame(user, userRepository.findByUsername("username"));
    }
}
