package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;

public interface PaymentStrategy {

    boolean pay(double amount, AcmePlexUser user);
}
