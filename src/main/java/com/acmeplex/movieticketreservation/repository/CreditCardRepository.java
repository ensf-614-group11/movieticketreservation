package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    CreditCard findByCardNumber();
}
