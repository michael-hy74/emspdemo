package com.demo.emsp.domain.annotation;

import com.demo.emsp.domain.enums.TokenType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TokenTypeValidator implements ConstraintValidator<ValidTokenType, String> {

    @Override
    public void initialize(ValidTokenType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            TokenType.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
