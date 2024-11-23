package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcmePlexCreditRepository extends JpaRepository<AcmePlexCredit, Integer> {

    Optional<AcmePlexCredit> findByCreditSecretCode(@NotBlank(message = "Credit secret code cannot be blank") String creditSecretCode);
}
