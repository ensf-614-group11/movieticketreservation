package com.acmeplex.movieticketreservation.services.strategy;

public interface RetrieveActivePaymentTypeStrategy<T> {
    T retrieve(Long userId);
}
