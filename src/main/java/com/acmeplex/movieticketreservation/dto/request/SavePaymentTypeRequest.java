package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SavePaymentTypeRequest {

    @NotBlank(message = "Payment type cannot be blank")
    private String type; // E.g., "CREDIT_CARD" or "PAYPAL"

    private String cardholderName;
    private String cardNumber;     // Optional, for credit card
    private String expirationDate; // Optional, for credit card
    private String cvv;            // Optional, for credit card
    private String email;          // Optional, for PayPal

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
