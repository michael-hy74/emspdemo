package com.demo.emsp.domain.annotation;

import com.demo.emsp.domain.enums.TokenStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TokenStatusValidator implements ConstraintValidator<ValidTokenStatus, String> {

    @Override
    public void initialize(ValidTokenStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            TokenStatus.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
