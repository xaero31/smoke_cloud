package com.ermakov.nikita.smokecloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * created by Nikita_Ermakov at 12/18/2019
 */
@Controller
public class MainController {

    @Value("${secret.property}")
    private String secret;

    @GetMapping("/welcome")
    public String getWelcomePage(Model model) {
        model.addAttribute("secret", secret);
        return "index";
    }
}
