package com.acmeplex.movieticketreservation.validation.validator.strategy;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import jakarta.validation.ConstraintValidatorContext;

public class CreatePayPalValidator implements CreatePaymentTypeValidator {

    @Override
    public boolean validate(CreatePaymentTypeRequest request, ConstraintValidatorContext context) {
        if (request.getEmail() == null || !request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            context.buildConstraintViolationWithTemplate("Valid email is required for PayPal").addConstraintViolation();
            return false;
        }
        return true;
    }
}
