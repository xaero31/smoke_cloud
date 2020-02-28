package com.ermakov.nikita.controller;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.ViewName;
import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.entity.profile.VerificationToken;
import com.ermakov.nikita.entity.security.User;
import com.ermakov.nikita.event.RegisterEvent;
import com.ermakov.nikita.model.RegisterForm;
import com.ermakov.nikita.repository.RoleRepository;
import com.ermakov.nikita.repository.UserRepository;
import com.ermakov.nikita.repository.VerificationTokenRepository;
import com.ermakov.nikita.service.api.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Collections;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Slf4j
@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileService profileService;
    private final VerificationTokenRepository tokenRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(@Autowired UserRepository userRepository,
                              @Autowired RoleRepository roleRepository,
                              @Autowired ProfileService profileService,
                              @Autowired VerificationTokenRepository tokenRepository,
                              @Autowired PasswordEncoder passwordEncoder,
                              @Autowired ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(path = ControllerPath.REGISTER, method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return ViewName.REGISTER;
    }

    @RequestMapping(path = ControllerPath.REGISTER, method = RequestMethod.POST)
    public String registerPerform(@ModelAttribute @Valid RegisterForm registerForm,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            return ViewName.REGISTER;
        }

        final User user = saveUser(registerForm);
        if (user != null) {
            saveProfile(registerForm, user);
            eventPublisher.publishEvent(new RegisterEvent(this, user));

            log.info("Registered new user: {}", user.getUsername());
            return ControllerPath.REDIRECT + ControllerPath.LOGIN;
        } else {
            model.addAttribute("registerError",
                    String.format("User %s already exists", registerForm.getUsername()));

            log.info("User {} already exists", registerForm.getUsername());
            return ViewName.REGISTER;
        }
    }

    @RequestMapping(path = ControllerPath.VERIFY_USER, method = RequestMethod.POST)
    public String verifyUser(@RequestParam("token") String token,
                             Model model) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (isTokenExpired(verificationToken)) {
            log.info("Token {} is expired. User has not been verified", token);

            model.addAttribute("error", "Verification link actual time is expired");
            return ControllerPath.LOGIN;
        }

        final User user = userRepository.findById(verificationToken.getUser().getId());

        user.setEnabled(true);
        userRepository.save(user);

        log.info("User {} was verified", user.getUsername());
        model.addAttribute("success", "Account was verified successfully");

        return ControllerPath.LOGIN;
    }

    private User saveUser(RegisterForm registerForm) {
        final User user = new User();

        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        user.setCredentialsNonExpired(true);
        user.setNonExpired(true);
        user.setNonLocked(true);

        return userRepository.saveUniqueUser(user);
    }

    private void saveProfile(RegisterForm registerForm, User user) {
        final Profile profile = new Profile();

        profile.setUser(user);
        profile.setFirstName(registerForm.getFirstName());
        profile.setLastName(registerForm.getLastName());
        profile.setMiddleName(registerForm.getMiddleName());

        profileService.save(profile);
    }

    private boolean isTokenExpired(VerificationToken token) {
        final Instant expireInstant = token.getExpirationDate().toInstant();
        final Instant now = Instant.now();

        return now.isAfter(expireInstant);
    }
}