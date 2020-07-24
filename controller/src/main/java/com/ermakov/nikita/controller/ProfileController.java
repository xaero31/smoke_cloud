package com.ermakov.nikita.controller;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.ViewName;
import com.ermakov.nikita.entity.profile.Profile;
import com.ermakov.nikita.service.api.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Nikita_Ermakov at 7/6/20
 */
@Controller("profileController")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(@Autowired @Qualifier("profileService") ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(path = ControllerPath.PROFILE)
    public String getProfilePage(Model model) {
        final Profile profile = getCurrentProfile();

        model.addAttribute("profile", profile);
        model.addAttribute("profileName", resolveProfileName(profile));

        return ViewName.PROFILE;
    }

    @GetMapping(path = ControllerPath.PROFILE_EDIT)
    public String getProfileEditPage(Model model) {
        final Profile profile = getCurrentProfile();

        model.addAttribute("profile", profile);
        model.addAttribute("profileName", resolveProfileName(profile));

        return ViewName.PROFILE_EDIT;
    }

    @GetMapping(ControllerPath.ROOT)
    public String getIndexPage() {
        return ControllerPath.REDIRECT + ControllerPath.PROFILE;
    }

    private Profile getCurrentProfile() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();

        return profileService.findByUserName(principal.getUsername());
    }

    private String resolveProfileName(Profile profile) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(profile.getFirstName());

        final String middleName = profile.getMiddleName();
        if (middleName != null && !middleName.isEmpty()) {
            stringBuilder.append(" ");
            stringBuilder.append(middleName);
        }

        stringBuilder.append(" ");
        stringBuilder.append(profile.getLastName());

        return stringBuilder.toString();
    }
}
