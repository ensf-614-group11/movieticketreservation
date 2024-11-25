package com.acmeplex.movieticketreservation.services.strategy;

import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.CreditCardRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("creditCardPayment")
public class CreditCardPayment implements PaymentStrategy {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final RetrieveCreditCardPaymentType retrieveCreditCardPaymentType;

    public CreditCardPayment(UserRepository userRepository,
                             CreditCardRepository creditCardRepository,
                             PaymentTypeService paymentTypeService,
                             RetrieveCreditCardPaymentType retrieveCreditCardPaymentType
    ) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
        this.retrieveCreditCardPaymentType = retrieveCreditCardPaymentType;
    }

    @Override
    public boolean unregisteredUserPay(UnregisteredUserPaymentRequest paymentRequest){
        String cardNumber = paymentRequest.getCreditCardNumber();
        String cardHolderName = paymentRequest.getFirstName() + " " + paymentRequest.getLastName();
        float amount = paymentRequest.getAmount();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName);
        return true;
    }

    public boolean registeredUserPay(RegisteredUserPaymentRequest paymentRequest){
        AcmePlexUser user = userRepository.findById(paymentRequest.getUserId()).get();

        List<PaymentType> paymentTypes = user.getPaymentTypes();

        CreditCard creditCard = null;
        for(PaymentType paymentType : paymentTypes){
            if(paymentType.getMethod().equals("CreditCard")){
                creditCard = retrieveCreditCardPaymentType.retrieve(user.getId());
            }
        }

        String cardNumber = creditCard.getCardNumber();
        String cardHolderName = user.getFirstName() + " " + user.getLastName();
        float amount = paymentRequest.getAmount();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName);
        return true;
    }

    public boolean registeredUserPay(Long userId, float amount){
        AcmePlexUser user = userRepository.findById(userId).get();

        List<PaymentType> paymentTypes = user.getPaymentTypes();

        CreditCard creditCard = null;
        for(PaymentType paymentType : paymentTypes){
            if(paymentType.getMethod().equals("CreditCard")){
                creditCard = retrieveCreditCardPaymentType.retrieve(user.getId());
            }
        }

        String cardNumber = creditCard.getCardNumber();
        String cardHolderName = user.getFirstName() + " " + user.getLastName();

        System.out.println("Processing credit card payment of $" + amount +
                "\nCard Number: " + cardNumber +
                "\nCard Holder Name: " + cardHolderName);
        return true;
    }

}

