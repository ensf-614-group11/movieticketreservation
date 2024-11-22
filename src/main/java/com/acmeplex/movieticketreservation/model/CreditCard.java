package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CreditCard extends PaymentType {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;

    public CreditCard() {}

    public CreditCard(float amount, String status, String method, AcmePlexUser acmePlexUser, String cardNumber, String cardHolderName, String expiryDate) {
        super(status, method, acmePlexUser);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}