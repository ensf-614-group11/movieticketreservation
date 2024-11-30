package com.acmeplex.movieticketreservation.factory;

import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.model.PaymentType;
import org.springframework.stereotype.Component;

@Component
public class CreatePaymentTypeFactory {

    public PaymentType createPaymentType(PaymentTypeRequest request) {
        PaymentType paymentType;

        switch (request.getType().toUpperCase()) {
            case "CREDIT_CARD":
                paymentType = createCreditCardPayment(request);
                break;

            default:
                throw new IllegalArgumentException("Unsupported payment type: " + request.getType());
        }

        paymentType.setType(request.getType());
        return paymentType;
    }

    private CreditCard createCreditCardPayment(PaymentTypeRequest request) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardHolderName(request.getCardholderName());
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setExpiryDate(request.getExpirationDate());
        return creditCard;
    }
}
