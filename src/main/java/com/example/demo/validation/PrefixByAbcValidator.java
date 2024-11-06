package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrefixByAbcValidator implements ConstraintValidator<PrefixByAbc, String> {

    private static final String PREFIX = "Abc";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.startsWith(PREFIX);
        }
        return true;
    }
}