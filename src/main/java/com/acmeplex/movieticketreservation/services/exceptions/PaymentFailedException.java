package com.acmeplex.movieticketreservation.services.exceptions;

/**
 * Custom exception to handle payment-related failures.
 */
public class PaymentFailedException extends RuntimeException {

    private String reason;

    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
