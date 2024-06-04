package com.exe01.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email format";

    String nullMessage() default "Email cannot be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
