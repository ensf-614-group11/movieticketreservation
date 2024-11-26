package com.acmeplex.movieticketreservation.facade;

import com.acmeplex.movieticketreservation.dto.request.CreateUserRequest;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.DefaultRegistrationRequest;
import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.repository.ReceiptRepository;
import com.acmeplex.movieticketreservation.services.*;
import com.acmeplex.movieticketreservation.services.exceptions.PaymentFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade for handling the registration flow, including user, billing address, and payment type management.
 */
@Service
public class RegistrationFacade {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationFacade.class);

    private final AcmePlexUserProfileService userProfileService;
    private final AddressService addressService;
    private final PaymentTypeService paymentTypeService;
    private final PaymentService paymentService;
    private final RegistrationPostProcessingService postProcessingService;
    private final NotificationService notificationService;
    @Autowired
    private ReceiptRepository receiptRepository;

    public RegistrationFacade(
            AcmePlexUserProfileService userProfileService,
            AddressService addressService,
            PaymentTypeService paymentTypeService,
            PaymentService paymentService,
            RegistrationPostProcessingService postProcessingService,
            NotificationService notificationService,
            ReceiptRepository receiptRepository) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
        this.paymentService = paymentService;
        this.postProcessingService = postProcessingService;
        this.notificationService = notificationService;
        this.receiptRepository = receiptRepository;
    }

    /**
     * Handles the complete registration process.
     *
     * @param registerRequest      The registration request with user
     * @return                     The registered AcmePlexUser.
     */
    @Transactional
    public AcmePlexUser registerUser(DefaultRegistrationRequest registerRequest) {
        CreateUserRequest createUserRequest = registerRequest.getUser();
        CreatePaymentTypeRequest paymentTypeRequest = registerRequest.getPaymentType();
        CreateBillingAddressRequest billingAddressRequest = registerRequest.getBillingAddress();

        try {
            logger.info("Starting user registration for email: {}", createUserRequest.getEmail());

            Payment payment = processPayment(createUserRequest, paymentTypeRequest, billingAddressRequest);
            AcmePlexUser newUser = registerAndFetchUser(createUserRequest);
            handlePostRegistration(newUser, payment);
            saveBillingAddress(newUser, billingAddressRequest);
            savePaymentType(newUser, paymentTypeRequest);

            logger.info("Registration process completed successfully for user: {}", createUserRequest.getEmail());

            finalizeRegistration(newUser, payment);
            return newUser;

        } catch (PaymentFailedException ex) {
            logger.error("Payment failure during registration for user: {}", createUserRequest.getEmail(), ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            logger.error("Validation error during registration for user: {}", createUserRequest.getEmail(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during registration for user: {}", createUserRequest.getEmail(), ex);
            throw new IllegalStateException("An unexpected error occurred during registration: " + ex.getMessage());
        }
    }

    private Payment processPayment(CreateUserRequest createUserRequest,
                                   CreatePaymentTypeRequest paymentTypeRequest,
                                   CreateBillingAddressRequest billingAddressRequest) {
        try {
            Payment payment = paymentService.processNewAcmePlexMembershipPayment(paymentTypeRequest, billingAddressRequest);
            logger.info("Payment processed successfully for email: {}", createUserRequest.getEmail());
            return payment;
        } catch (PaymentFailedException ex) {
            logger.error("Payment failed for email: {}", createUserRequest.getEmail(), ex);
            throw new PaymentFailedException("Payment processing failed: " + ex.getMessage());
        }
    }

    private AcmePlexUser registerAndFetchUser(CreateUserRequest createUserRequest) {
        try {
            userProfileService.registerUser(createUserRequest);
            AcmePlexUser newUser = userProfileService.getUserByEmail(createUserRequest.getEmail());
            logger.debug("User created: {}", newUser.getEmail());
            return newUser;
        } catch (IllegalArgumentException ex) {
            logger.error("User registration failed for email: {}", createUserRequest.getEmail(), ex);
            throw new IllegalArgumentException("User registration failed: " + ex.getMessage());
        }
    }

    private void handlePostRegistration(AcmePlexUser newUser, Payment payment) {
        try {
            postProcessingService.handlePostRegistration(newUser, payment);
        } catch (Exception ex) {
            logger.error("Post-registration processing failed for user: {}", newUser.getEmail(), ex);
            throw new IllegalStateException("Failed during post-registration processing: " + ex.getMessage());
        }
    }

    private void saveBillingAddress(AcmePlexUser newUser, CreateBillingAddressRequest billingAddressRequest) {
        try {
            Address billingAddress = addressService.createBillingAddress(newUser.getEmail(), billingAddressRequest);
            newUser.setSavedBillingAddress(billingAddress);
            logger.debug("Billing address saved: {}", billingAddress.getStreet());
        } catch (Exception ex) {
            logger.error("Failed to save billing address for user: {}", newUser.getEmail(), ex);
            throw new IllegalStateException("Failed to save billing address: " + ex.getMessage());
        }
    }

    private void savePaymentType(AcmePlexUser newUser, CreatePaymentTypeRequest paymentTypeRequest) {
        try {
            PaymentType paymentType = paymentTypeService.createPaymentType(newUser.getEmail(), paymentTypeRequest);
            paymentTypeService.setActivePaymentType(newUser.getEmail(), paymentType.getId());
            logger.debug("Payment type saved: {}", paymentType.getType());
        } catch (Exception ex) {
            logger.error("Failed to save payment type for user: {}", newUser.getEmail(), ex);
            throw new IllegalStateException("Failed to save payment type: " + ex.getMessage());
        }
    }

    private void finalizeRegistration(AcmePlexUser newUser, Payment payment) {
        Receipt membershipReceipt = receiptRepository.findByPaymentId(payment.getId());
        notificationService.sendAnnualFeePaymentConfirmation(newUser, membershipReceipt);
        notificationService.addObserver(newUser);
        logger.info("Finalized registration for user: {}", newUser.getEmail());
    }
}
