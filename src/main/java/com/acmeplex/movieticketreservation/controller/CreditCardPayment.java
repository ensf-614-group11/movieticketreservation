package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.CreditCard;
import org.springframework.stereotype.Component;

@Component
public class CreditCardPayment implements PaymentStrategy{

    private CreditCard creditCard;

    public CreditCardPayment(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public boolean pay(double amount, AcmePlexUser user){
        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + creditCard.getCardNumber() +
                "\nCard Holder Name: " + creditCard.getCardHolderName());
        return true;
    }

}
