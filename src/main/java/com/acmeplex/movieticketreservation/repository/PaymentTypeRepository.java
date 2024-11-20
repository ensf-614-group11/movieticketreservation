package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    /**
     * Finds the active payment type for a user by their ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the active PaymentType if found, or empty if not found
     */
    @Query("SELECT p FROM PaymentType p WHERE p.acmePlexUser.id = :userId AND p.status = 'Active'")
    Optional<PaymentType> findActivePaymentTypeByUserId(@Param("userId") Long userId);

    /**
     * Finds a payment type for a user based on the payment method type and card number.
     *
     * @param userId    The ID of the user.
     * @param type      The payment method type (e.g., "CREDIT_CARD", "PAYPAL").
     * @param cardNumber The card number associated with the payment type.
     * @return An {@link Optional} containing the matching {@link PaymentType}, or empty if no match is found.
     */
    @Query("SELECT p FROM PaymentType p WHERE p.acmePlexUser.id = :userId AND p.method = :type AND p.cardNumber = :cardNumber")
    Optional<PaymentType> findByUserIdAndDetails(@Param("userId") Long userId,
                                                 @Param("type") String type,
                                                 @Param("cardNumber") String cardNumber);

}
