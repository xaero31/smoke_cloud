package com.ermakov.nikita.service.api;

import com.ermakov.nikita.entity.profile.Profile;

/**
 * created by Nikita_Ermakov at 2/21/2020
 */
public interface ProfileService {

    Profile save(Profile profile);

    Profile findByUserName(String username);
}