package com.acmeplex.movieticketreservation.validation.validator;

import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.validation.annotation.CreateValidPaymentType;
import com.acmeplex.movieticketreservation.validation.factory.PaymentTypeValidatorFactory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePaymentTypeValidator implements ConstraintValidator<CreateValidPaymentType, PaymentTypeRequest> {

    @Autowired
    private PaymentTypeValidatorFactory factory;

    @Autowired
    private AuthUserPrincipalService authUserPrincipalService;

    @Override
    public boolean isValid(PaymentTypeRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getType() == null) {
            context.buildConstraintViolationWithTemplate("Payment type cannot be null").addConstraintViolation();
            return false;
        }

        context.disableDefaultConstraintViolation();
        try {
            // Resolve the appropriate countryCode
            String countryCode = resolveCountryCode(request);

            // Get the validator and validate the request
            com.acmeplex.movieticketreservation.validation.validator.strategy.CreatePaymentTypeValidator validator = factory.getValidator(request.getType(), countryCode);
            return validator.validate(request, context);
        } catch (IllegalArgumentException e) {
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
    }

    /**
     * Resolves the country code dynamically based on the authentication state.
     * If the user is authenticated, use their country code.
     * If not authenticated, use the country code from the request.
     *
     * @param request the CreatePaymentTypeRequest
     * @return the resolved country code
     */
    private String resolveCountryCode(PaymentTypeRequest request) {
        if (authUserPrincipalService.isAuthenticated()) {
            // Use the logged-in user's country code
            return authUserPrincipalService.getCurrentUserPrincipal().getCountryCode().name();
        }

        // Validate and use the country code from the request for unauthenticated users
        if (request.getCountryCode() == null || request.getCountryCode().isBlank()) {
            throw new IllegalArgumentException("Country code must be provided for unauthenticated users.");
        }

        return request.getCountryCode();
    }
}

