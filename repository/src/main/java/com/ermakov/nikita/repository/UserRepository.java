package com.ermakov.nikita.repository;

import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.repository.custom.UserRepositoryCustom;
import org.springframework.data.repository.Repository;

/**
 * created by Nikita_Ermakov at 2/27/2020
 */
public interface UserRepository extends Repository<User, Long>, UserRepositoryCustom {

    User findById(long id);

    User save(User user);
}
