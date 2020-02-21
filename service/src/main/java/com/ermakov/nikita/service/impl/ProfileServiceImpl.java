package com.ermakov.nikita.service.impl;

import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.repository.ProfileRepository;
import com.ermakov.nikita.service.api.ProfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Nikita_Ermakov at 2/21/2020
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    public ProfileServiceImpl(@Autowired ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile save(Profile profile) {
        if (profile == null) {
            return null;
        }

        profile.setFirstName(capitalizeWord(profile.getFirstName()));
        profile.setLastName(capitalizeComplexString(profile.getLastName()));
        profile.setMiddleName(capitalizeWord(profile.getMiddleName()));

        return profileRepository.save(profile);
    }

    private String capitalizeComplexString(String complexString) {
        if (complexString == null) {
            return null;
        }

        final String[] words = complexString.split("-");
        for (int i = 0; i < words.length; i++) {
            words[i] = capitalizeWord(words[i]);
        }

        return StringUtils.join(words, '-');
    }

    private String capitalizeWord(String word) {
        if (word != null) {
            return StringUtils.capitalize(StringUtils.lowerCase(word));
        } else {
            return null;
        }
    }
}