package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexMembership;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Payment;
import com.acmeplex.movieticketreservation.model.Receipt;
import com.acmeplex.movieticketreservation.repository.PaymentRepository;
import com.acmeplex.movieticketreservation.repository.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles post-registration tasks, such as membership creation.
 */
@Service
public class RegistrationPostProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationPostProcessingService.class);

    private final AcmePlexMembershipCreationService membershipService;
    private final AcmePlexUserProfileService userProfileService;
    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;

    public RegistrationPostProcessingService(
            AcmePlexMembershipCreationService membershipService,
            AcmePlexUserProfileService userProfileService,
            PaymentRepository paymentRepository,
            ReceiptRepository receiptRepository) {
        this.membershipService = membershipService;
        this.userProfileService = userProfileService;
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
    }


    /**
     * Handles post-registration tasks for a newly registered user.
     *
     * @param user    The registered user.
     * @param payment The payment made during registration.
     * @return The updated AcmePlexUser object with all associations.
     */
    @Transactional
    public void handlePostRegistration(AcmePlexUser user, Payment payment) {
        logger.info("Handling post-registration tasks for user: {}", user.getEmail());

        // Step 1: Create and associate membership
        AcmePlexMembership membership = membershipService.createMembership(user.getDateRegistered());
        user.setMembership(membership); // Associate membership with the user
        logger.debug("Membership created and associated with user: {}", user.getEmail());

        // Step 2: Associate payment with the user
        payment.setAcmePlexUser(user);
        paymentRepository.save(payment); // Update the payment entity
        logger.debug("Payment associated with user: {}", user.getEmail());

        // Step 3: Associate receipt with the user
        Receipt receipt = receiptRepository.findByPaymentId(payment.getId());
        if (receipt != null) {
            receipt.setAcmePlexUser(user);
            receiptRepository.save(receipt); // Update the receipt entity
            logger.debug("Receipt associated with user: {}", user.getEmail());
        }

        userProfileService.save(user);

        logger.info("Post-registration tasks completed for user: {}", user.getEmail());
    }
}
