package com.ermakov.nikita.validation.validator;

import com.ermakov.nikita.model.RegisterForm;
import com.ermakov.nikita.validation.annotation.PasswordConfirm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        if (value.getPassword() != null) {
            final boolean isValid = value.getPassword().equals(value.getConfirmPassword());

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation();
            }

            return isValid;
        }

        return false;
    }
}
