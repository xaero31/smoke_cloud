package com.ermakov.nikita.model;

import lombok.Data;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Data
public class RegisterForm {
    
    private String username;
    private String password;
    private String confirmPassword;

    private String firstName;
    private String lastName;
    private String middleName;
}
