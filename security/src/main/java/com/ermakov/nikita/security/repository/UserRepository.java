package com.ermakov.nikita.security.repository;

import com.ermakov.nikita.model.security.User;
import com.ermakov.nikita.model.security.User_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * created by Nikita_Ermakov at 2/16/2020
 */
@Slf4j
@Repository
public class UserRepository {

    private EntityManager em;

    public UserRepository(@Autowired EntityManager em) {
        this.em = em;
    }

    public User findByUsername(String username) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(userRoot);
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(User_.USERNAME), username));

        try {
            return em.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            log.info("Not found user with username {}", username);
            return null;
        }
    }
}
