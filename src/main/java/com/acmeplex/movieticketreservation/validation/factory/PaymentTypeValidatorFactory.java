package com.acmeplex.movieticketreservation.validation.factory;

import com.acmeplex.movieticketreservation.model.CountryCode;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import com.acmeplex.movieticketreservation.validation.validator.strategy.CanadaCreditCardValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.PayPalValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.PaymentTypeValidator;
import com.acmeplex.movieticketreservation.validation.validator.strategy.USCreditCardValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentTypeValidatorFactory {

    private final AuthUserPrincipalService authUserPrincipalService;
    private final Map<String, PaymentTypeValidator> validators = new HashMap<>();

    public PaymentTypeValidatorFactory(AuthUserPrincipalService authUserPrincipalService) {
        this.authUserPrincipalService = authUserPrincipalService;

        // Register country-specific validators for CREDIT_CARD
        validators.put("CREDIT_CARD:US", new USCreditCardValidator());
        validators.put("CREDIT_CARD:CA", new CanadaCreditCardValidator());

        // Register a default validator for PayPal or other types
        validators.put("PAYPAL", new PayPalValidator());
    }

    public PaymentTypeValidator getValidator(String type, String countryCodeFromRequest) {
        CountryCode resolvedCountryCode = resolveCountryCode(countryCodeFromRequest);

        String key = type.toUpperCase() + ":" + resolvedCountryCode.name();
        PaymentTypeValidator validator = validators.get(key);

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
