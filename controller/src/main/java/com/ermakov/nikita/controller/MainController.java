package com.ermakov.nikita.controller;

import com.ermakov.nikita.ControllerPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Nikita_Ermakov at 12/18/2019
 *
 * todo remove after. Controller for testing some things
 */
@Controller("mainController")
public class MainController {

    @GetMapping(ControllerPath.ROOT)
    public String getWelcomePage() {
        return ControllerPath.REDIRECT + ControllerPath.PROFILE;
    }
}
