package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.entity.security.Role;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.model.RegisterForm;
import com.ermakov.nikita.repository.ProfileRepository;
import com.ermakov.nikita.repository.RoleRepository;
import com.ermakov.nikita.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @Test
    void registerGetShouldReturnRegisterFormInModel() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(model().attributeExists("registerForm"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void registerPostShouldCheckUserAndInsertNewUserByRepositories() throws Exception {
        final RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("user");
        registerForm.setPassword("pass");
        registerForm.setConfirmPassword("pass");
        registerForm.setFirstName("firstName");
        registerForm.setLastName("lastName");
        registerForm.setMiddleName("middleName");

        when(userRepository.saveUser(any(User.class))).thenReturn(new User());
        when(roleRepository.findByName(anyString())).thenReturn(new Role());
        when(profileRepository.save(any(Profile.class))).thenReturn(new Profile());

        System.out.println(profileRepository == null);

        mockMvc.perform(post("/register")
                .flashAttr("registerForm", registerForm)
                .with(csrf()))
                .andExpect(redirectedUrl("/login"))
                .andReturn();

        verify(profileRepository, atLeastOnce()).save(any(Profile.class));
    }
}