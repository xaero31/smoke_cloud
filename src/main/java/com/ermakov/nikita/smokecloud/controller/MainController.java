package com.ermakov.nikita.smokecloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Nikita_Ermakov at 12/18/2019
 */
@Controller
public class MainController {

    @GetMapping("/welcome")
    public String getWelcomePage() {
        return "index";
    }
}
