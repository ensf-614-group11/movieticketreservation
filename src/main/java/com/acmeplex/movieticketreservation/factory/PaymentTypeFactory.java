package com.acmeplex.movieticketreservation.factory;

import com.acmeplex.movieticketreservation.dto.request.SavePaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.model.PaymentType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentTypeFactory {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    public PaymentType createPaymentType(SavePaymentTypeRequest request) {
        if (request.getType().equalsIgnoreCase("CREDIT_CARD")) {
            return createCreditCardPayment(request);

            // Add more cases for other payment types, e.g., PayPal, Bank Transfer
        }
        throw new IllegalArgumentException("Unsupported payment type: " + request.getType());
    }

    private CreditCard createCreditCardPayment(SavePaymentTypeRequest request) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardHolderName(request.getCardholderName());
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setExpiryDate(LocalDate.parse(request.getExpirationDate(), DATE_FORMATTER));
        return creditCard;
    }
}
