package com.acmeplex.movieticketreservation.validation.strategy;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import jakarta.validation.ConstraintValidatorContext;

public interface PaymentTypeValidator {
    boolean validate(CreatePaymentTypeRequest request, ConstraintValidatorContext context);
}
