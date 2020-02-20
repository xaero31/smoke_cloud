package com.ermakov.nikita.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Data
// todo custom check same passwords annotation
public class RegisterForm {

    @NotEmpty(message = "Username couldn't be empty")
    @NotNull(message = "Username couldn't be null")
    @Size(min = 4, max = 50, message = "Username size could be between {min} and {max}")
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "Username could contain only letters and numbers")
    private String username;

    @NotEmpty(message = "Password couldn't be empty")
    @NotNull(message = "Password couldn't be null")
    @Size(min = 6, max = 255, message = "Password size could be between {min} and {max}")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "FirstName couldn't be empty")
    @NotNull(message = "FirstName couldn't be null")
    @Size(min = 2, max = 50, message = "FirstName size could be between {min} and {max}")
    @Pattern(regexp = "[A-ZА-Я][a-zа-я]+", message = "FirstName could contain only letters")
    private String firstName;

    @NotEmpty(message = "LastName couldn't be empty")
    @NotNull(message = "LastName couldn't be null")
    @Size(min = 2, max = 100, message = "LastName size could be between {min} and {max}")
    @Pattern(regexp = "([A-ZА-Я][a-zа-я]+)(-[A-ZА-Я][a-zа-я]+)?",
            message = "LastName could contain only letters and '-'")
    private String lastName;

    @Size(min = 3, max = 100, message = "MiddleName size could be between {min} and {max}")
    @Pattern(regexp = "[A-ZА-Я][a-zа-я]+", message = "MiddleName could contain only letters")
    private String middleName;
}
