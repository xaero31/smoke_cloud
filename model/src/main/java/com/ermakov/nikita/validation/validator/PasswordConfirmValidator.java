package com.ermakov.nikita.validation.validator;

import com.ermakov.nikita.model.RegisterForm;
import com.ermakov.nikita.validation.annotation.PasswordConfirm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        if (value.getPassword() != null) {
            return value.getPassword().equals(value.getConfirmPassword());
        }

        return false;
    }
}
