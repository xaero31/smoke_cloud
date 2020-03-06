package com.ermakov.nikita.repository.custom;

import com.ermakov.nikita.entity.security.User;

/**
 * created by Nikita_Ermakov at 2/27/2020
 */
public interface UserRepositoryCustom {

    User findByUsername(String username);

    User saveUniqueUser(User user);
}
