package com.acmeplex.movieticketreservation.validation.validator.strategy;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import jakarta.validation.ConstraintValidatorContext;

public class CreateUSCreditCardValidator implements CreatePaymentTypeValidator {

    @Override
    public boolean validate(CreatePaymentTypeRequest request, ConstraintValidatorContext context) {
        StringBuilder errorMessage = new StringBuilder();

        if (request.getCardholderName() == null || request.getCardholderName().isBlank()) {
            errorMessage.append("Cardholder name is required for US Credit Card. ");
        }
        if (request.getCardNumber() == null || !request.getCardNumber().matches("\\d{16}")) {
            errorMessage.append("Valid 16-digit card number is required for US Credit Card. ");
        }
        if (request.getExpirationDate() == null || !request.getExpirationDate().matches("\\d{2}/\\d{2}")) {
            errorMessage.append("Expiration date (MM/YY) is required for US Credit Card. ");
        }
        if (request.getCvv() == null || !request.getCvv().matches("\\d{3}")) {
            errorMessage.append("Valid 3-digit CVV is required for US Credit Card. ");
        }

        if (!errorMessage.isEmpty()) {
            context.buildConstraintViolationWithTemplate(errorMessage.toString().trim()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
