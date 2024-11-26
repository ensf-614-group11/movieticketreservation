package com.acmeplex.movieticketreservation.facade;

import com.acmeplex.movieticketreservation.dto.request.CreateUserRequest;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final RegistrationPostProcessingService postProcessingService;
    private final PaymentService paymentService;

    public RegistrationFacade(
            AcmePlexUserProfileService userProfileService,
            AddressService addressService,
            PaymentTypeService paymentTypeService,
            RegistrationPostProcessingService postProcessingService,
            PaymentService paymentService) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
        this.postProcessingService = postProcessingService;
        this.paymentService = paymentService;
    }

    /**
     * Handles the complete registration process.
     *
     * @param createUserRequest      The registration request with user details.
     * @param billingAddressRequest  The billing address details.
     * @param paymentTypeRequest     The payment type details.
     * @return The registered AcmePlexUser.
     * @throws PaymentFailedException if the payment fails.
     */
    @Transactional
    public AcmePlexUser registerUser(CreateUserRequest createUserRequest,
                                     CreateBillingAddressRequest billingAddressRequest,
                                     CreatePaymentTypeRequest paymentTypeRequest) {
        try {
            logger.info("Starting user registration for email: {}", createUserRequest.getEmail());

            // Step 1: Process payment
            paymentService.processNewAcmePlexMembershipPayment(paymentTypeRequest, billingAddressRequest);
            logger.info("Payment processed successfully for email: {}", createUserRequest.getEmail());

            // Step 2: Register the user
            AcmePlexUser user = userProfileService.registerUser(createUserRequest);
            logger.debug("User registered successfully: {}", user.getEmail());

            // Step 3: Create billing address
            Address billingAddress = addressService.createBillingAddress(user.getEmail(), billingAddressRequest);
            user.setSavedBillingAddress(billingAddress);
            logger.debug("Billing address saved: {}", billingAddress.getStreet());

            // Step 4: Post-registration tasks (e.g., membership creation)
            postProcessingService.handlePostRegistration(user);

            logger.info("Registration process completed successfully for user: {}", createUserRequest.getEmail());
            return user;
        } catch (Exception ex) {
            logger.error("Error during registration workflow for user: {}", createUserRequest.getEmail(), ex);
            throw ex; // Re-throw the exception to roll back the transaction
        }
    }
}
