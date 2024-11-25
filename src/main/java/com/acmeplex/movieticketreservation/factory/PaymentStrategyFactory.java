package com.acmeplex.movieticketreservation.factory;

import com.acmeplex.movieticketreservation.services.strategy.PaymentStrategy;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentStrategyFactory {

    private final PaymentStrategy creditCardPayment;
    private final UserRepository userRepository;

    public PaymentStrategyFactory(@Qualifier("creditCardPayment") PaymentStrategy creditCardPayment, UserRepository userRepository) {
        this.creditCardPayment = creditCardPayment;
        this.userRepository = userRepository;
    }

    public PaymentStrategy getStrategy(UnregisteredUserPaymentRequest request) {
        if (request.getCreditCardNumber() != null) {
            return creditCardPayment;
        }
        throw new IllegalArgumentException("Unsupported payment method.");
    }

    // Overloaded method for Registered User payment
    public PaymentStrategy getStrategy(RegisteredUserPaymentRequest request) {
        Optional<AcmePlexUser> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()){
            throw new IllegalArgumentException("User not found.");
        }else{
            List<PaymentType> paymentTypes = user.get().getPaymentTypes();
            boolean userHasCreditCardPaymentType = false;
            for(PaymentType paymentType : paymentTypes){
                if(paymentType.getMethod().equals("CreditCard")){
                    userHasCreditCardPaymentType = true;
                }
            }
            if(userHasCreditCardPaymentType){
                return creditCardPayment;
            }
            throw new IllegalArgumentException("Unsupported payment method.");
        }
    }

    public PaymentStrategy getStrategy(Long userId){
        Optional<AcmePlexUser> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new IllegalArgumentException("User not found.");
        }else{
            List<PaymentType> paymentTypes = user.get().getPaymentTypes();
            boolean userHasCreditCardPaymentType = false;
            for(PaymentType paymentType : paymentTypes){
                if(paymentType.getMethod().equals("CreditCard")){
                    userHasCreditCardPaymentType = true;
                }
            }
            if(userHasCreditCardPaymentType){
                return creditCardPayment;
            }
            throw new IllegalArgumentException("Unsupported payment method.");
        }


    }
}
