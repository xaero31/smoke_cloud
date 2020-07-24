package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.service.api.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * created by Nikita_Ermakov at 7/19/20
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        profile = new Profile();

        lenient().when(profileService.findByUserName("username"))
                .thenReturn(profile);
    }

    @Test
    @WithMockUser(username = "username")
    void profilePageShouldHaveProfileEntity() throws Exception {
        mockMvc.perform(get(ControllerPath.PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "username")
    void profilePageShouldHaveProfileFullName() throws Exception {
        profile.setFirstName("Maksim");
        profile.setLastName("Lepyohin");
        profile.setMiddleName("Gennadievich");

        mockMvc.perform(get(ControllerPath.PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attribute("profileName", "Maksim Gennadievich Lepyohin"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "username")
    void profilePageShouldHaveFirstNameAndLastNameIfMiddleNameIsNull() throws Exception {
        profile.setFirstName("Maksim");
        profile.setLastName("Lepyohin");

        mockMvc.perform(get(ControllerPath.PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attribute("profileName", "Maksim Lepyohin"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "username")
    void editProfileShouldReturnEditProfilePage() throws Exception {
        profile.setFirstName("Maksim");
        profile.setLastName("Lepyohin");

        mockMvc.perform(get(ControllerPath.PROFILE_EDIT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().attributeExists("profileName"))
                .andReturn();
    }
}
