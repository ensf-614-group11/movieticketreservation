package com.acmeplex.movieticketreservation.services.strategy;

import com.acmeplex.movieticketreservation.dto.request.BillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;

public interface PaymentStrategy {

    boolean unregisteredUserPay(UnregisteredUserPaymentRequest paymentRequest);

    boolean registeredUserPay(RegisteredUserPaymentRequest paymentRequest);

    boolean registeredUserPay(Long userId, float amount);

    boolean newAcmePlexMembershipPay(
            PaymentTypeRequest paymentTypeRequest,
            BillingAddressRequest billingAddressRequest);
}
