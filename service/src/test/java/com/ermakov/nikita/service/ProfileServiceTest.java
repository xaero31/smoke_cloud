package com.ermakov.nikita.service;

import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.repository.ProfileRepository;
import com.ermakov.nikita.repository.UserRepository;
import com.ermakov.nikita.service.api.ProfileService;
import com.ermakov.nikita.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * created by Nikita_Ermakov at 2/21/2020
 */
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    private ProfileService profileService;
    private Profile profile;

    @BeforeEach
    void before() {
        profileService = new ProfileServiceImpl(profileRepository, userRepository);
        profile = new Profile();

        lenient().when(profileRepository.save(any(Profile.class))).then(invocation -> invocation.getArgument(0));
    }

    @Test
    void profileServiceShouldSaveFirstNameAsCapitalizedWord() {
        profile.setFirstName("fiRsTnaMe");
        final Profile result = profileService.save(profile);

        assertEquals("Firstname", result.getFirstName());
    }

    @Test
    void profileServiceShouldSaveLastNameAsCapitalizedWord() {
        profile.setLastName("laStNamE");
        final Profile result = profileService.save(profile);

        assertEquals("Lastname", result.getLastName());
    }

    @Test
    void profileServiceShouldSaveComplexLastNameAsCapitalizedWords() {
        profile.setLastName("comPlEx-lAstName");
        final Profile result = profileService.save(profile);

        assertEquals("Complex-Lastname", result.getLastName());
    }

    @Test
    void profileServiceShouldSaveMiddleNameAsCapitalizedWord() {
        profile.setMiddleName("miDdLenAme");
        final Profile result = profileService.save(profile);

        assertEquals("Middlename", result.getMiddleName());
    }

    @Test
    void profileServiceShouldWorkProperlyWithNullMiddleName() {
        profile.setMiddleName(null);
        final Profile result = profileService.save(profile);

        assertNotNull(result);
    }

    @Test
    void profileServiceShouldFindProfileByUsername() {
        final User user = new User();
        final Profile profile = new Profile();

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(profileRepository.findByUser(user)).thenReturn(profile);

        final Profile actualProfile = profileService.findByUserName("testUser");

        assertSame(profile, actualProfile);
    }
}