package com.acmeplex.movieticketreservation.validation.annotation;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.validation.factory.PaymentTypeValidatorFactory;
import com.acmeplex.movieticketreservation.validation.strategy.PaymentTypeValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SavePaymentTypeValidator implements ConstraintValidator<ValidPaymentType, CreatePaymentTypeRequest> {

    @Autowired
    private PaymentTypeValidatorFactory factory;

    @Override
    public boolean isValid(CreatePaymentTypeRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getType() == null) {
            context.buildConstraintViolationWithTemplate("Payment type cannot be null").addConstraintViolation();
            return false;
        }

        context.disableDefaultConstraintViolation();
        try {
            PaymentTypeValidator validator = factory.getValidator(request.getType());
            return validator.validate(request, context);
        } catch (IllegalArgumentException e) {
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
    }
}
