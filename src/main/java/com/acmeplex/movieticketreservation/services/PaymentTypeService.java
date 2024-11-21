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
     * Deactivates any existing active payment type for a user.
     *
     * @param userId The ID of the user.
     */
    private void deactivateExistingActivePayment(Long userId) {
        paymentTypeRepository.findActivePaymentTypeByUserId(userId)
                .ifPresent(existingActivePayment -> {
                    existingActivePayment.setStatus("Inactive");
                    paymentTypeRepository.save(existingActivePayment);
                });
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

        deactivateExistingActivePayment(user.getId());

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);
        paymentType.setStatus("Active");
        paymentType.setMethod(request.getType());

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
