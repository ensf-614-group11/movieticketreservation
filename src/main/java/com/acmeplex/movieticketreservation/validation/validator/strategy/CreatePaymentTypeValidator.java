package com.acmeplex.movieticketreservation.validation.validator.strategy;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import jakarta.validation.ConstraintValidatorContext;

public interface CreatePaymentTypeValidator {
    boolean validate(CreatePaymentTypeRequest request, ConstraintValidatorContext context);
}
