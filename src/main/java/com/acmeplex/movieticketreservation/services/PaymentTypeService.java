package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.factory.CreatePaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.PaymentTypeRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentTypeService {

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
        var activePaymentOpt = paymentTypeRepository.findActivePaymentTypeByUserId(userId);

        if (activePaymentOpt.isEmpty()) {
            return;
        }

        PaymentType activePayment = activePaymentOpt.get();
        var allPayments = paymentTypeRepository.findAllByUserId(userId);

        if (allPayments.size() <= 1) {
            return;
        }

        PaymentType alternativePayment = allPayments.stream()
                .filter(payment -> !payment.equals(activePayment))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No alternative payment types available for activation."));

        activePayment.setStatus("Inactive");
        alternativePayment.setStatus("Active");

        paymentTypeRepository.save(activePayment);
        paymentTypeRepository.save(alternativePayment);
    }

    @Transactional
    public PaymentType createPaymentType(String email, CreatePaymentTypeRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);
        paymentType.setStatus("Inactive");
        paymentType.setMethod(request.getType());

        return paymentTypeRepository.save(paymentType);
    }

    @Transactional
    public PaymentType setActivePaymentType(String email, Long paymentTypeId) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Payment type not found with ID: " + paymentTypeId));

        if (!paymentType.getAcmePlexUser().equals(user)) {
            throw new IllegalArgumentException("Payment type does not belong to the user.");
        }

        deactivateExistingActivePayment(user.getId());
        paymentType.setStatus("Active");
        return paymentTypeRepository.save(paymentType);
    }

    @Transactional(readOnly = true)
    public List<PaymentType> getPaymentTypesByUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return paymentTypeRepository.findAllByAcmePlexUserId(user.getId());
    }

    @Transactional
    public PaymentType updateOrCreateAndSetActive(String email, CreatePaymentTypeRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        var matchingPaymentTypeOpt = paymentTypeRepository.findByUserIdAndDetails(
                user.getId(),
                request.getType(),
                request.getCardNumber()
        );

        PaymentType paymentType;

        if (matchingPaymentTypeOpt.isPresent()) {
            paymentType = matchingPaymentTypeOpt.get();
        } else {
            paymentType = paymentTypeFactory.createPaymentType(request);
            paymentType.setAcmePlexUser(user);
            paymentType.setStatus("Inactive");
            paymentTypeRepository.save(paymentType);
        }

        deactivateExistingActivePayment(user.getId());
        paymentType.setStatus("Active");
        return paymentTypeRepository.save(paymentType);
    }

    @Transactional(readOnly = true)
    public PaymentType getActivePaymentTypeByEmail(String email) {
        AcmePlexUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Optional<PaymentType> activePaymentType = paymentTypeRepository.findActivePaymentTypeByUserId(user.getId());
        return activePaymentType.orElseThrow(() -> new IllegalArgumentException("No active payment type found for user with email: " + email));
    }

    @Transactional(readOnly = true)
    public PaymentType findActivePaymentTypeByUserId(Long userId) {
        Optional<PaymentType> activePaymentType = paymentTypeRepository.findActivePaymentTypeByUserId(userId);
        return activePaymentType.orElseThrow(() -> new IllegalArgumentException("No active payment type found for user with ID: " + userId));
    }

    public <T extends PaymentType> T getActivePaymentTypeAs(Long userId, Class<T> type) {
        PaymentType activePaymentType = findActivePaymentTypeByUserId(userId);

        if (type.isInstance(activePaymentType)) {
            return type.cast(activePaymentType);
        } else {
            throw new IllegalArgumentException("Active payment type is not of the expected type: " + type.getSimpleName());
        }
    }
}
