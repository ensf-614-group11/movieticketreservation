package com.acmeplex.movieticketreservation.strategy;

public interface RetrieveActivePaymentTypeStrategy<T> {
    T retrieve(Long userId);
}
