package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.PaymentRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;

public interface PaymentStrategy {

    boolean pay(PaymentRequest paymentRequest);
}
