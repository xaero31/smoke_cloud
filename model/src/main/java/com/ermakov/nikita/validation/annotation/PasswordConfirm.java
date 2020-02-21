package com.ermakov.nikita.validation.annotation;

import com.ermakov.nikita.validation.validator.PasswordConfirmValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConfirmValidator.class)
@Documented
public @interface PasswordConfirm {

    String message() default "Input passwords are different, but should be the same";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
