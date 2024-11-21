package com.acmeplex.movieticketreservation.validation.factory;

import com.acmeplex.movieticketreservation.validation.strategy.CreditCardValidator;
import com.acmeplex.movieticketreservation.validation.strategy.PayPalValidator;
import com.acmeplex.movieticketreservation.validation.strategy.PaymentTypeValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentTypeValidatorFactory {

    private final Map<String, PaymentTypeValidator> validators = Map.of(
            "CREDIT_CARD", new CreditCardValidator(),
            "PAYPAL", new PayPalValidator()
    );

    public PaymentTypeValidator getValidator(String type) {
        PaymentTypeValidator validator = validators.get(type.toUpperCase());
        if (validator == null) {
            throw new IllegalArgumentException("Unsupported payment type: " + type);
        }
        return validator;
    }
}
