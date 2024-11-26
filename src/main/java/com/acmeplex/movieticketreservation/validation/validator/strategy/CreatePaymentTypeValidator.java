package com.acmeplex.movieticketreservation.validation.validator.strategy;

import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import jakarta.validation.ConstraintValidatorContext;

public interface CreatePaymentTypeValidator {
    boolean validate(PaymentTypeRequest request, ConstraintValidatorContext context);
}
