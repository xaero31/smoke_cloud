package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.controller.RegisterController;
import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.Role;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.event.RegisterEvent;
import com.ermakov.nikita.model.RegisterForm;
import com.ermakov.nikita.queue.listener.RegisterEventListener;
import com.ermakov.nikita.repository.ProfileRepository;
import com.ermakov.nikita.repository.RoleRepository;
import com.ermakov.nikita.repository.UserRepository;
import com.ermakov.nikita.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
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
    private RegisterEventListener registerEventListener;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private ProfileRepository profileRepository;
    @MockBean
    private VerificationTokenRepository tokenRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private RegisterController registerController;
    private RegisterForm registerForm;

    @BeforeEach
    void before() {
        registerForm = new RegisterForm();

        registerForm.setUsername("username");
        registerForm.setPassword("password");
        registerForm.setConfirmPassword("password");
        registerForm.setEmail("email@email.com");
        registerForm.setFirstName("Firstname");
        registerForm.setLastName("Lastname");
        registerForm.setMiddleName("Middlename");

        lenient().when(userRepository.saveUniqueUser(any(User.class))).thenReturn(new User());
        lenient().when(roleRepository.findByName(anyString())).thenReturn(new Role());
        lenient().when(profileRepository.save(any(Profile.class))).thenReturn(new Profile());
        lenient().when(passwordEncoder.encode(anyString())).then(invocation -> invocation.getArgument(0));
    }

    @Test
    void registerGetShouldReturnRegisterFormInModel() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(model().attributeExists("registerForm"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void registerPostShouldCheckUserAndInsertNewUserByRepositories() throws Exception {
        testRegisterPostForNotExistingErrors();
        verify(profileRepository, atLeastOnce()).save(any(Profile.class));
    }

    @Test
    void registerPostShouldPublishRegisterEvent() throws Exception {
        doNothing().when(registerEventListener).sendMailConfirmingNotification(any(RegisterEvent.class));
        testRegisterPostForNotExistingErrors();
        verify(registerEventListener, atLeastOnce()).sendMailConfirmingNotification(any(RegisterEvent.class));
    }

    private void testRegisterPostForNotExistingErrors() throws Exception {
        mockMvc.perform(post("/register")
                .flashAttr("registerForm", registerForm)
                .with(csrf()))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(redirectedUrl("/login"))
                .andReturn();
    }

    @Test
    void registerUserWithExistingUserNameShouldReturnErrors() throws Exception {
        when(userRepository.saveUniqueUser(any(User.class))).thenReturn(null);
        mockMvc.perform(post("/register")
                .flashAttr("registerForm", registerForm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerError"))
                .andReturn();
    }

    @Test
    void registerWithNullUsernameShouldReturnErrors() throws Exception {
        registerForm.setUsername(null);
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithEmptyUsernameShouldReturnErrors() throws Exception {
        registerForm.setUsername("");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithShortUsernameShouldReturnErrors() throws Exception {
        registerForm.setUsername("abc");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithLongUsernameShouldReturnErrors() throws Exception {
        final char[] usernameCharArray = new char[300];
        Arrays.fill(usernameCharArray, 's');
        registerForm.setUsername(new String(usernameCharArray));

        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithUsernameWithNotOnlyLettersAndNumbersShouldReturnErrors() throws Exception {
        registerForm.setUsername("(-fwfqd");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithEmptyPasswordShouldReturnErrors() throws Exception {
        registerForm.setPassword("");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithNullPasswordShouldReturnErrors() throws Exception {
        registerForm.setPassword(null);
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithVeryLongPasswordShouldReturnErrors() throws Exception {
        final char[] passwordCharArray = new char[300];
        Arrays.fill(passwordCharArray, 'p');
        registerForm.setPassword(new String(passwordCharArray));

        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithVeryShortPasswordShouldReturnErrors() throws Exception {
        registerForm.setPassword("abcde");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithNotMatchingPasswordsShouldReturnErrors() throws Exception {
        registerForm.setConfirmPassword("abcde");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithNullFirstNameShouldReturnErrors() throws Exception {
        registerForm.setFirstName(null);
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithEmptyFirstNameShouldReturnErrors() throws Exception {
        registerForm.setFirstName("");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithShortFirstNameShouldReturnErrors() throws Exception {
        registerForm.setFirstName("a");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithVeryLongFirstNameShouldReturnErrors() throws Exception {
        final char[] firstNameCharArray = new char[300];
        Arrays.fill(firstNameCharArray, 'd');
        registerForm.setFirstName(new String(firstNameCharArray));

        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithFirstNameNotOblyOfLettersShouldReturnErrors() throws Exception {
        registerForm.setFirstName("ads2-");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithNullLastNameShouldReturnErrors() throws Exception {
        registerForm.setLastName(null);
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithEmptyLastNameShouldReturnErrors() throws Exception {
        registerForm.setLastName("");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithShortLastNameShouldReturnErrors() throws Exception {
        registerForm.setLastName("t");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithLongLastNameShouldReturnErrors() throws Exception {
        final char[] lastNameCharArray = new char[300];
        Arrays.fill(lastNameCharArray, 'f');
        registerForm.setLastName(new String(lastNameCharArray));

        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithLastNameNotOnlyOfLettersAndMinusShouldReturnErrors() throws Exception {
        registerForm.setLastName("ada11=");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithShortMiddleNameShouldReturnErrors() throws Exception {
        registerForm.setMiddleName("af");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithMiddleNameNotOnlyOfLettersShouldReturnErrors() throws Exception {
        registerForm.setLastName("ada11=");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithLongMiddleNameShouldReturnErrors() throws Exception {
        final char[] middleNameCharArray = new char[300];
        Arrays.fill(middleNameCharArray, 'f');
        registerForm.setLastName(new String(middleNameCharArray));

        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithNullMiddleNameShouldWorkProperly() throws Exception {
        registerForm.setMiddleName(null);
        testRegisterPostForNotExistingErrors();
    }

    @Test
    void registerWithEmptyMiddleNameShouldWorkProperly() throws Exception {
        registerForm.setMiddleName("");
        testRegisterPostForNotExistingErrors();
    }

    @Test
    void registerWithNullEmailShouldReturnErrors() throws Exception {
        registerForm.setEmail(null);
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithEmptyEmailShouldReturnErrors() throws Exception {
        registerForm.setEmail("");
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithLongMailShouldReturnErrors() throws Exception {
        final char[] mailCharArray = new char[300];
        Arrays.fill(mailCharArray, 'm');
        registerForm.setEmail(new String(mailCharArray));
        testRegisterPostForExistingErrors();
    }

    @Test
    void registerWithMailNotMeetPatternShouldReturnErrors() throws Exception {
        registerForm.setEmail("random email");
        testRegisterPostForExistingErrors();
    }

    @Test
    void testFillUserInfoFromRegisterForm() {
        reset(userRepository);
        when(userRepository.saveUniqueUser(any(User.class))).then(invocation -> {
            final User user = invocation.getArgument(0, User.class);

            assertEquals(registerForm.getUsername(), user.getUsername());
            assertEquals(registerForm.getPassword(), user.getPassword());
            assertEquals(registerForm.getEmail(), user.getEmail());

            return user;
        });
        registerController.registerPerform(registerForm, mock(BindingResult.class), mock(Model.class));
    }

    @Test
    void testFillProfileInfoFromRegisterForm() {
        final User user = new User();
        reset(profileRepository, userRepository);
        when(userRepository.saveUniqueUser(any(User.class))).thenReturn(user);
        when(profileRepository.save(any(Profile.class))).then(invocation -> {
            final Profile profile = invocation.getArgument(0, Profile.class);

            assertEquals(registerForm.getFirstName(), profile.getFirstName());
            assertEquals(registerForm.getMiddleName(), profile.getMiddleName());
            assertEquals(registerForm.getLastName(), profile.getLastName());
            assertSame(user, profile.getUser());

            return profile;
        });
        registerController.registerPerform(registerForm, mock(BindingResult.class), mock(Model.class));
    }

    @Test
    void registerControllerShouldSetEnabledToTrueAndSaveUser() {
        final String token = "token";
        final User user = new User();
        user.setId(1337);
        user.setEnabled(false);

        final VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(Date.from(Instant.now().plus(1L, ChronoUnit.DAYS)));

        final Model model = mock(Model.class);

        when(tokenRepository.findByToken(eq("token"))).thenReturn(verificationToken);
        when(userRepository.findById(1337)).thenReturn(user);

        registerController.verifyUser(token, model);

        assertTrue(user.isEnabled(), "Verify user method should enable user");
        verify(userRepository, atLeastOnce()).save(user);
        verify(model).addAttribute(eq("success"), anyString());
    }

    @Test
    void registerControllerShouldNotSaveUserWhenExpirationDateIsExpired() {
        final String token = "token";
        final User user = new User();
        user.setId(1337);
        user.setEnabled(false);

        final VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(Date.from(Instant.now().minus(1L, ChronoUnit.MINUTES)));

        final Model model = mock(Model.class);

        when(tokenRepository.findByToken(eq("token"))).thenReturn(verificationToken);

        registerController.verifyUser(token, model);

        verify(userRepository, never()).save(user);
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    void registerControllerShouldReturnErrorWhenTokenDoesNotExist() {
        final Model model = mock(Model.class);

        when(tokenRepository.findByToken(anyString())).thenReturn(null);

        registerController.verifyUser("token", model);

        verify(userRepository, never()).save(any(User.class));
        verify(model).addAttribute(eq("error"), anyString());
    }

    private void testRegisterPostForExistingErrors() throws Exception {
        mockMvc.perform(post("/register")
                .flashAttr("registerForm", registerForm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("registerForm"))
                .andReturn();
    }
}