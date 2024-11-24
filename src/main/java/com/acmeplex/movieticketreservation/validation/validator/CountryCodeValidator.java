package com.acmeplex.movieticketreservation.validation.validator;

import com.acmeplex.movieticketreservation.model.CountryCode;
import com.acmeplex.movieticketreservation.validation.annotation.ValidCountryCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            CountryCode.valueOf(value.toUpperCase());
            return true; // Valid country code
        } catch (IllegalArgumentException e) {
            return false; // Invalid country code
        }
    }
}
