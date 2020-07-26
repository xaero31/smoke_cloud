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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Collections;
import java.util.Locale;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Slf4j
@Controller("registerController")
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileService profileService;
    private final VerificationTokenRepository tokenRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public RegisterController(@Autowired @Qualifier("userRepository") UserRepository userRepository,
                              @Autowired @Qualifier("roleRepository") RoleRepository roleRepository,
                              @Autowired @Qualifier("profileService") ProfileService profileService,
                              @Autowired @Qualifier("verificationTokenRepository")
                                      VerificationTokenRepository tokenRepository,
                              @Autowired @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder,
                              @Autowired ApplicationEventPublisher eventPublisher,
                              @Autowired MessageSource messageSource) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
    }

    @RequestMapping(path = ControllerPath.REGISTER, method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return ViewName.REGISTER;
    }

    @RequestMapping(path = ControllerPath.REGISTER, method = RequestMethod.POST)
    public String registerPerform(@ModelAttribute @Valid RegisterForm registerForm,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes,
                                  Locale locale) {
        if (result.hasErrors()) {
            return ViewName.REGISTER;
        }

        final User existingUser = userRepository.findByUsername(registerForm.getUsername());
        if (existingUser == null) {
            User newUser = fillUser(registerForm);

            newUser = userRepository.save(newUser);
            saveProfile(registerForm, newUser);

            eventPublisher.publishEvent(new RegisterEvent(this, newUser, locale));

            final String username = newUser.getUsername();
            redirectAttributes.addFlashAttribute("registerMessage", messageSource.getMessage(
                    "register.user.registered", new Object[]{username, newUser.getEmail()}, locale));

            log.info("Registered new user: {}", username);
            return ControllerPath.REDIRECT + ControllerPath.LOGIN;
        }

        if (existingUser.isEnabled()) {
            final String username = existingUser.getUsername();

            model.addAttribute("registerError",
                    String.format(messageSource.getMessage("register.user.exists", new Object[]{username}, locale),
                            username));

            log.info("User {} already exists", username);
            return ViewName.REGISTER;
        } else {
            eventPublisher.publishEvent(new RegisterEvent(this, existingUser, locale));

            final String username = existingUser.getUsername();
            final String email = existingUser.getEmail();

            redirectAttributes.addFlashAttribute("registerMessage", messageSource.getMessage(
                    "register.user.disabled", new Object[]{username, email}, locale));

            log.info("User {} exists. Sending email activation link to {}.", username, email);
            return ControllerPath.REDIRECT + ControllerPath.LOGIN;
        }
    }

    @RequestMapping(path = ControllerPath.VERIFY_USER, method = RequestMethod.GET)
    public String verifyUser(@RequestParam("token") String token,
                             Model model,
                             Locale locale) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            log.info("Token {} does not exist", token);

            model.addAttribute("error",
                    messageSource.getMessage("verify.token.not.exists", null, locale));
            return ViewName.LOGIN;
        }

        if (isTokenExpired(verificationToken)) {
            log.info("Token {} is expired. User has not been verified", token);

            model.addAttribute("error",
                    messageSource.getMessage("verify.link.expired", null, locale));
            return ViewName.LOGIN;
        }

        final User user = userRepository.findById(verificationToken.getUser().getId());

        user.setEnabled(true);
        userRepository.save(user);

        log.info("User {} was verified", user.getUsername());
        model.addAttribute("success", messageSource.getMessage("verify.success", null, locale));

        return ViewName.LOGIN;
    }

    private User fillUser(RegisterForm registerForm) {
        final User user = new User();

        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        user.setCredentialsNonExpired(true);
        user.setNonExpired(true);
        user.setNonLocked(true);

        return user;
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