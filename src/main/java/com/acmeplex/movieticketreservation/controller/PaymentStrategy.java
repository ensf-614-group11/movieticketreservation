package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.dto.request.UnregisteredUserPaymentRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;

public interface PaymentStrategy {

    boolean unregisteredUserPay(UnregisteredUserPaymentRequest paymentRequest);

    boolean registeredUserPay(RegisteredUserPaymentRequest paymentRequest);
}
