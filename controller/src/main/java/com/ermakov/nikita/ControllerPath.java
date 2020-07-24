package com.ermakov.nikita;

public interface ControllerPath {

    String REDIRECT = "redirect:";

    String ROOT = "/";

    String REGISTER = "/register";
    String VERIFY_USER = "/verifyUser";
    String LOGIN = "/login";

    String PROFILE = "/profile";
    String PROFILE_EDIT = PROFILE + "/edit";

    String LOGOUT = "/logout";
}