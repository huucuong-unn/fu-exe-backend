package com.exe01.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String PHONE_PATTERN = "^(\\+84|0)\\d{9,10}$";
    private String nullMessage;
    private String invalidFormatMessage;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        nullMessage = constraintAnnotation.nullMessage();
        invalidFormatMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            handleValidationFailure(context, nullMessage);
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        if (!pattern.matcher(phone).matches()) {
            handleValidationFailure(context, invalidFormatMessage);
            return false;
        }

        return true;
    }

    private void handleValidationFailure(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
