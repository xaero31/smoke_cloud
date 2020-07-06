package com.ermakov.nikita.controller;

import com.ermakov.nikita.ControllerPath;
import com.ermakov.nikita.ViewName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Nikita_Ermakov at 7/6/20
 */
@Controller("profileController")
public class ProfileController {

    @GetMapping(path = ControllerPath.PROFILE)
    public String getProfilePage() {
//        todo add page filling
        return ViewName.PROFILE;
    }
}
