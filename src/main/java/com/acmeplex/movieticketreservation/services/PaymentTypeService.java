package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.factory.CreatePaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.PaymentTypeRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentTypeService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTypeService.class);

    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final CreatePaymentTypeFactory paymentTypeFactory;

    public PaymentTypeService(
            PaymentTypeRepository paymentTypeRepository,
            UserRepository userRepository,
            CreatePaymentTypeFactory paymentTypeFactory) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.userRepository = userRepository;
        this.paymentTypeFactory = paymentTypeFactory;
    }

    private void deactivateExistingActivePayment(Long userId) {
        logger.info("Deactivating existing active payment for userId: {}", userId);
        var activePaymentOpt = paymentTypeRepository.findActivePaymentTypeByUserId(userId);

        if (activePaymentOpt.isEmpty()) {
            logger.info("No active payment found for userId: {}", userId);
            return;
        }

        PaymentType activePayment = activePaymentOpt.get();
        var allPayments = paymentTypeRepository.findAllByUserId(userId);

        if (allPayments.size() <= 1) {
            logger.info("Only one payment method exists for userId: {}, no action needed.", userId);
            return;
        }

        PaymentType alternativePayment = allPayments.stream()
                .filter(payment -> !payment.equals(activePayment))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("No alternative payment types available for activation for userId: {}", userId);
                    return new IllegalStateException("No alternative payment types available for activation.");
                });

        activePayment.setStatus("Inactive");
        alternativePayment.setStatus("Active");

        paymentTypeRepository.save(activePayment);
        paymentTypeRepository.save(alternativePayment);

        logger.info("Deactivated payment {} and activated payment {} for userId: {}", activePayment.getId(), alternativePayment.getId(), userId);
    }

    @Transactional
    public PaymentType createPaymentType(String email, PaymentTypeRequest request) {
        logger.info("Creating payment type for user with email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);
        paymentType.setStatus("Inactive");
        paymentType.setType(request.getType());

        PaymentType savedPaymentType = paymentTypeRepository.save(paymentType);
        logger.info("Created payment type {} for user with email: {}", savedPaymentType.getId(), email);
        return savedPaymentType;
    }

    @Transactional
    public PaymentType setActivePaymentType(String email, Long paymentTypeId) {
        logger.info("Setting active payment type for user with email: {}, paymentTypeId: {}", email, paymentTypeId);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> {
                    logger.error("Payment type not found with ID: {}", paymentTypeId);
                    return new IllegalArgumentException("Payment type not found with ID: " + paymentTypeId);
                });

        if (!paymentType.getAcmePlexUser().equals(user)) {
            logger.error("Payment type {} does not belong to user with email: {}", paymentTypeId, email);
            throw new IllegalArgumentException("Payment type does not belong to the user.");
        }

        deactivateExistingActivePayment(user.getId());
        paymentType.setStatus("Active");
        PaymentType updatedPaymentType = paymentTypeRepository.save(paymentType);

        logger.info("Set payment type {} as active for user with email: {}", updatedPaymentType.getId(), email);
        return updatedPaymentType;
    }

    @Transactional(readOnly = true)
    public List<PaymentType> getPaymentTypesByUser(String email) {
        logger.info("Retrieving payment types for user with email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        List<PaymentType> paymentTypes = paymentTypeRepository.findAllByAcmePlexUserId(user.getId());
        logger.info("Retrieved {} payment types for user with email: {}", paymentTypes.size(), email);
        return paymentTypes;
    }

    @Transactional
    public PaymentType updateOrCreateAndSetActive(String email, PaymentTypeRequest request) {
        logger.info("Updating or creating and setting active payment type for user with email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        var matchingPaymentTypeOpt = paymentTypeRepository.findByUserIdAndDetails(
                user.getId(),
                request.getType(),
                request.getCardNumber()
        );

        PaymentType paymentType;

        if (matchingPaymentTypeOpt.isPresent()) {
            logger.info("Found matching payment type for user with email: {}", email);
            paymentType = matchingPaymentTypeOpt.get();
        } else {
            logger.info("No matching payment type found for user with email: {}, creating new.", email);
            paymentType = paymentTypeFactory.createPaymentType(request);
            paymentType.setAcmePlexUser(user);
            paymentType.setStatus("Inactive");
            paymentTypeRepository.save(paymentType);
        }

        deactivateExistingActivePayment(user.getId());
        paymentType.setStatus("Active");
        PaymentType updatedPaymentType = paymentTypeRepository.save(paymentType);

        logger.info("Updated or created and set payment type {} as active for user with email: {}", updatedPaymentType.getId(), email);
        return updatedPaymentType;
    }

    @Transactional(readOnly = true)
    public PaymentType getActivePaymentTypeByEmail(String email) {
        logger.info("Retrieving active payment type for user with email: {}", email);
        AcmePlexUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        Optional<PaymentType> activePaymentType = paymentTypeRepository.findActivePaymentTypeByUserId(user.getId());
        PaymentType result = activePaymentType.orElseThrow(() -> {
            logger.error("No active payment type found for user with email: {}", email);
            return new IllegalArgumentException("No active payment type found for user with email: " + email);
        });

        logger.info("Retrieved active payment type {} for user with email: {}", result.getId(), email);
        return result;
    }

    @Transactional(readOnly = true)
    public PaymentType findActivePaymentTypeByUserId(Long userId) {
        logger.info("Retrieving active payment type for userId: {}", userId);
        Optional<PaymentType> activePaymentType = paymentTypeRepository.findActivePaymentTypeByUserId(userId);

        PaymentType result = activePaymentType.orElseThrow(() -> {
            logger.error("No active payment type found for userId: {}", userId);
            return new IllegalArgumentException("No active payment type found for user with ID: " + userId);
        });

        logger.info("Retrieved active payment type {} for userId: {}", result.getId(), userId);
        return result;
    }

    public <T extends PaymentType> T getActivePaymentTypeAs(Long userId, Class<T> type) {
        logger.info("Retrieving active payment type for userId: {} as type: {}", userId, type.getSimpleName());
        PaymentType activePaymentType = findActivePaymentTypeByUserId(userId);

        if (type.isInstance(activePaymentType)) {
            logger.info("Active payment type for userId: {} is of expected type: {}", userId, type.getSimpleName());
            return type.cast(activePaymentType);
        } else {
            logger.error("Active payment type for userId: {} is not of expected type: {}", userId, type.getSimpleName());
            throw new IllegalArgumentException("Active payment type is not of the expected type: " + type.getSimpleName());
        }
    }
}
