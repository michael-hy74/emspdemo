package com.demo.emsp.domain.annotation;

import com.demo.emsp.domain.enums.TokenStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountStatusValidator implements ConstraintValidator<ValidAccountStatus, String> {

    @Override
    public void initialize(ValidAccountStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            TokenStatus.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
