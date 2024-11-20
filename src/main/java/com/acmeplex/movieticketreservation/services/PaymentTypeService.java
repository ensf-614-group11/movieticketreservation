package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.factory.PaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.CreditCard;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.PaymentTypeRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import com.acmeplex.movieticketreservation.utils.PaymentTypeDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Service layer for managing payment types, including creation, updates, deactivation, and removal.
 * This service ensures that only one active payment type exists per user at any given time.
 */
@Service
public class PaymentTypeService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTypeService.class);

    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final PaymentTypeFactory paymentTypeFactory;

    private final Map<Class<? extends PaymentType>, BiConsumer<PaymentType, UpdatePaymentTypeRequest>> updateStrategies = new HashMap<>();

    public PaymentTypeService(
            PaymentTypeRepository paymentTypeRepository,
            UserRepository userRepository,
            PaymentTypeFactory paymentTypeFactory) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.userRepository = userRepository;
        this.paymentTypeFactory = paymentTypeFactory;
        registerUpdateStrategies();
    }

    private void registerUpdateStrategies() {
        updateStrategies.put(CreditCard.class, (paymentType, request) -> {
            CreditCard creditCard = (CreditCard) paymentType;
            creditCard.setCardHolderName(request.getCardholderName());
            creditCard.setCardNumber(request.getCardNumber());
            creditCard.setExpiryDate(PaymentTypeDateUtil.parseMMYYToLocalDate(request.getExpirationDate()));
        });
    }

    private void deactivateExistingActivePayment(Long userId, Long excludePaymentTypeId) {
        paymentTypeRepository.findActivePaymentTypeByUserId(userId)
                .ifPresent(existingActivePayment -> {
                    if (excludePaymentTypeId == null || !existingActivePayment.getId().equals(excludePaymentTypeId)) {
                        existingActivePayment.setStatus("Inactive");
                        paymentTypeRepository.save(existingActivePayment);
                    }
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

        deactivateExistingActivePayment(user.getId(), null);

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);
        paymentType.setStatus("Active");
        paymentType.setMethod(request.getType());

        return paymentTypeRepository.save(paymentType);
    }

    /**
     * Updates or creates a payment type for a user.
     *
     * @param email   The email of the user.
     * @param request The request data for updating or creating the payment type.
     * @return The updated or newly created {@link PaymentType}.
     */
    @Transactional
    public PaymentType updateOrCreatePaymentType(String email, UpdatePaymentTypeRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if a payment type with the same details already exists for the user
        var existingPaymentTypeOpt = paymentTypeRepository.findByUserIdAndDetails(
                user.getId(),
                request.getType(),
                request.getCardNumber()
        );

        if (existingPaymentTypeOpt.isPresent()) {
            // If the payment type exists, deactivate the current active payment type and activate this one
            PaymentType existingPaymentType = existingPaymentTypeOpt.get();
            deactivateExistingActivePayment(user.getId(), existingPaymentType.getId());
            existingPaymentType.setStatus("Active");
            return paymentTypeRepository.save(existingPaymentType);
        } else {
            // If the payment type does not exist, create a new one using CreatePaymentTypeRequest
            CreatePaymentTypeRequest createRequest = new CreatePaymentTypeRequest(
                    request.getType(),
                    request.getCardNumber(),
                    request.getCardholderName(),
                    request.getExpirationDate()
            );
            return createPaymentType(email, createRequest);
        }
    }
}
