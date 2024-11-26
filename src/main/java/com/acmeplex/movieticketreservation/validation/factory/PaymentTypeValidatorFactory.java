package com.acmeplex.movieticketreservation.validation.factory;

import com.acmeplex.movieticketreservation.model.CountryCode;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import com.acmeplex.movieticketreservation.validation.validator.strategy.CreateCanadaCreditCardValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.CreatePayPalValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.CreatePaymentTypeValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.CreateUSCreditCardValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentTypeValidatorFactory {

    private final AuthUserPrincipalService authUserPrincipalService;
    private final Map<String, CreatePaymentTypeValidator> validators = new HashMap<>();

    public PaymentTypeValidatorFactory(AuthUserPrincipalService authUserPrincipalService) {
        this.authUserPrincipalService = authUserPrincipalService;

        // Register country-specific validators for CreditCard
        validators.put("CreditCard:US", new CreateUSCreditCardValidator());
        validators.put("CreditCard:CA", new CreateCanadaCreditCardValidator());

        // Register a default validator for PayPal or other types
        validators.put("PayPal", new CreatePayPalValidator());
    }

    public CreatePaymentTypeValidator getValidator(String type, String countryCodeFromRequest) {
        CountryCode resolvedCountryCode = resolveCountryCode(countryCodeFromRequest);

        String key = type.toUpperCase() + ":" + resolvedCountryCode.name();
        CreatePaymentTypeValidator validator = validators.get(key);

        if (validator == null) {
            throw new IllegalArgumentException("Unsupported payment type or country: " + key);
        }
        return validator;
    }

    private CountryCode resolveCountryCode(String countryCodeFromRequest) {
        UserPrincipal currentUser = authUserPrincipalService.getCurrentUserPrincipal();

        if (currentUser != null) {
            // Use the logged-in user's country code
            return currentUser.getCountryCode();
        }

        // If no user is logged in, validate and use the request's country code
        if (countryCodeFromRequest == null || countryCodeFromRequest.isBlank()) {
            throw new IllegalArgumentException("Country code must be provided for unauthenticated users.");
        }

        return CountryCode.fromString(countryCodeFromRequest);
    }
}
