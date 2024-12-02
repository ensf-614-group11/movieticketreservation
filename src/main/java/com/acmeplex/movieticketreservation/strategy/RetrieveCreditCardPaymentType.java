package com.acmeplex.movieticketreservation.strategy;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
import org.springframework.stereotype.Component;

@Component
public class RetrieveCreditCardPaymentType implements RetrieveActivePaymentTypeStrategy<CreditCard> {

    private final PaymentTypeService paymentTypeService;

    public RetrieveCreditCardPaymentType(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    }

    @Override
    public CreditCard retrieve(Long userId) {
        return paymentTypeService.getActivePaymentTypeAs(userId, CreditCard.class);
    }
}
