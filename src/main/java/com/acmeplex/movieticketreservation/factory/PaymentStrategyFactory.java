package com.acmeplex.movieticketreservation.factory;

import com.acmeplex.movieticketreservation.controller.PaymentStrategy;
import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    private final PaymentStrategy creditCardPayment;

    public PaymentStrategyFactory(@Qualifier("creditCardPayment") PaymentStrategy creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }

    public PaymentStrategy getStrategy(PaymentRequest paymentRequest) {
        if (paymentRequest.getCreditCardNumber() != null) {
            return creditCardPayment;
        }
        throw new IllegalArgumentException("Unsupported payment method.");
    }
}
