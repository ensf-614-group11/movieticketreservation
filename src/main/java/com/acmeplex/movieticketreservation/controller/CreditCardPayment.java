package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import org.springframework.stereotype.Component;

@Component("creditCardPayment")
public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(PaymentRequest paymentRequest){
        String cardNumber = paymentRequest.getCreditCardNumber();
        String cardHolderName = paymentRequest.getFirstName() + " " + paymentRequest.getLastName();
        float amount = paymentRequest.getAmount();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName);
        return true;
    }

}

