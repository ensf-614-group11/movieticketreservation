package com.acmeplex.movieticketreservation.dto.response;

public class PaymentTypeResponse {

    private String type; // Payment type (e.g., "CREDIT_CARD", "PAYPAL")
    private String method; // Specific method (e.g., "CREDIT_CARD", "PAYPAL")
    private String cardholderName; // Credit card holder's name (if applicable)
    private String cardNumber; // Masked card number (if applicable)
    private String expirationDate; // Expiration date for credit cards (if applicable)
    private String email; // PayPal email (if applicable)

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
