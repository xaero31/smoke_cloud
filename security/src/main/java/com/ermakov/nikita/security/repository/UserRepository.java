package com.ermakov.nikita.security.repository;

import com.ermakov.nikita.entity.security.Role;
import com.ermakov.nikita.entity.security.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
@Slf4j
@Repository
public class UserRepository {

    @Value("${user.repository.find.by.username.query}")
    private String findByUsernameQuery = "";

    @Value("${user.repository.fetch.roles.privileges.query}")
    private String fetchPrivilegesQuery = "";

    private EntityManager em;

    public UserRepository(@Autowired EntityManager em) {
        this.em = em;
    }

    public User findByUsername(String username) {
        try {
            final User user = em.createQuery(findByUsernameQuery, User.class)
                    .setParameter(1, username)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getSingleResult();
            final List<Role> fetchedPrivilegesRoles = em.createQuery(fetchPrivilegesQuery, Role.class)
                    .setParameter(1, user.getRoles())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
            user.setRoles(fetchedPrivilegesRoles);

            log.info("Found user {}", username);
            return user;
        } catch (NoResultException e) {
            log.info("Not found user with username {}", username);
            return null;
        }
    }

    @Transactional
    public User saveUser(User user) {
        final String username = user.getUsername();
        if (findByUsername(username) != null) {
            return null;
        }

        em.persist(user);
        return user;
    }
}