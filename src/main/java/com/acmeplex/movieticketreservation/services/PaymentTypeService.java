package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.factory.PaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.PaymentTypeRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing payment types, including creation, activation, and deactivation.
 */
@Service
public class PaymentTypeService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTypeService.class);

    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final PaymentTypeFactory paymentTypeFactory;

    public PaymentTypeService(
            PaymentTypeRepository paymentTypeRepository,
            UserRepository userRepository,
            PaymentTypeFactory paymentTypeFactory) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.userRepository = userRepository;
        this.paymentTypeFactory = paymentTypeFactory;
    }

    /**
     * Deactivates any existing active payment type for a user, ensuring that the user always has at least one active payment type.
     *
     * @param userId The ID of the user.
     * @throws IllegalStateException if the user has no other payment types to activate.
     */
    private void deactivateExistingActivePayment(Long userId) {
        var activePaymentOpt = paymentTypeRepository.findActivePaymentTypeByUserId(userId);

        // If no active payment exists, there's nothing to deactivate
        if (activePaymentOpt.isEmpty()) {
            return;
        }

        PaymentType activePayment = activePaymentOpt.get();

        // Check if there are other payment types for the user
        var allPayments = paymentTypeRepository.findAllByUserId(userId);

        // If there's only one payment type, keep it active
        if (allPayments.size() <= 1) {
            throw new IllegalStateException("User must have at least one active payment type.");
        }

        // Find another payment type to activate
        PaymentType alternativePayment = allPayments.stream()
                .filter(payment -> !payment.equals(activePayment))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No alternative payment types available for activation."));

        // Deactivate the current active payment and activate the alternative
        activePayment.setStatus("Inactive");
        alternativePayment.setStatus("Active");

        paymentTypeRepository.save(activePayment);
        paymentTypeRepository.save(alternativePayment);
    }


    /**
     * Creates a new payment type for a user.
     *
     * @param email   The email of the user.
     * @param request The request data for creating the payment type.
     * @return The created {@link PaymentType}.
     */
    @Transactional
    public PaymentType createPaymentType(String email, CreatePaymentTypeRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);
        paymentType.setStatus("Inactive"); // Default to inactive; activation is handled separately
        paymentType.setMethod(request.getType());

        logger.info("Created new payment type for user: {}", email);
        return paymentTypeRepository.save(paymentType);
    }

    /**
     * Sets the specified payment type as active.
     *
     * @param email        The email of the user.
     * @param paymentTypeId The ID of the payment type to activate.
     * @return The updated {@link PaymentType}.
     */
    @Transactional
    public PaymentType setActivePaymentType(String email, Long paymentTypeId) {
        // Find the user by email
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Find the payment type by ID
        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Payment type not found with ID: " + paymentTypeId));

        // Verify that the payment type belongs to the authenticated user
        if (!paymentType.getAcmePlexUser().equals(user)) {
            throw new IllegalArgumentException("Payment type does not belong to the user.");
        }

        // Deactivate any other active payment types for the user
        deactivateExistingActivePayment(user.getId());

        // Set the selected payment type as active
        paymentType.setStatus("Active");
        return paymentTypeRepository.save(paymentType);
    }

    /**
     * Retrieves all payment types for a specific user.
     *
     * @param email The email of the user.
     * @return A list of {@link PaymentType} associated with the user.
     * @throws UserNotFoundException if the user with the specified email does not exist.
     */
    @Transactional(readOnly = true)
    public List<PaymentType> getPaymentTypesByUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return paymentTypeRepository.findAllByAcmePlexUserId(user.getId());
    }
}
