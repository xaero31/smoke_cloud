package com.ermakov.nikita.model;

import com.ermakov.nikita.validation.annotation.PasswordConfirm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@Data
@PasswordConfirm
public class RegisterForm {

    @NotEmpty(message = "Username couldn't be empty")
    @NotNull(message = "Username couldn't be null")
    @Size(min = 4, max = 50, message = "Username size should be between {min} and {max} symbols")
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "Username should contains only letters and numbers")
    private String username;

    @NotEmpty(message = "Password couldn't be empty")
    @NotNull(message = "Password couldn't be null")
    @Size(min = 6, max = 255, message = "Password size should be between {min} and {max} symbols")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "FirstName couldn't be empty")
    @NotNull(message = "FirstName couldn't be null")
    @Size(min = 2, max = 50, message = "FirstName size should be between {min} and {max} symbols")
    @Pattern(regexp = "[A-ZА-Яa-zа-я]+",
            message = "FirstName should contains only letters and starts with capitalize letter")
    private String firstName;

    @NotEmpty(message = "LastName couldn't be empty")
    @NotNull(message = "LastName couldn't be null")
    @Size(min = 2, max = 100, message = "LastName size should be between {min} and {max} symbols")
    @Pattern(regexp = "([A-ZА-Яa-zа-я]+)(-[A-ZА-Яa-zа-я]+)?",
            message = "LastName should contains only letters and '-' and starts with capitalize letter")
    private String lastName;

    @Size(max = 100, message = "MiddleName size should be less than {max} symbols")
    @Pattern(regexp = "[A-ZА-Яa-zа-я]{3,}|^$",
            message = "MiddleName should contains only letters and starts with capitalize letter size more than 3 symbols")
    private String middleName;
}
