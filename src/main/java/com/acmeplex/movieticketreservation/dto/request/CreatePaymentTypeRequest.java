package com.acmeplex.movieticketreservation.dto.request;

import com.acmeplex.movieticketreservation.validation.annotation.ValidPaymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@ValidPaymentType
public class CreatePaymentTypeRequest {

    @NotBlank(message = "Payment type cannot be blank")
    private String type; // E.g., "CREDIT_CARD" or "PAYPAL"

    // Credit card-specific fields
    private String cardholderName;

    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\d{2}$", message = "Expiration date must be in MMYY format")
    private String expirationDate;

    @Pattern(regexp = "\\d{3,4}", message = "CVV must be 3 or 4 digits")
    private String cvv;

    // PayPal-specific fields
    @Email(message = "Invalid email format for PayPal")
    private String email;

    // Default constructor
    public CreatePaymentTypeRequest() {
    }

    // Constructor for PaymentTypeService
    public CreatePaymentTypeRequest(String type, String cardNumber, String cardholderName, String expirationDate) {
        this.type = type;
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expirationDate = expirationDate;
    }

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