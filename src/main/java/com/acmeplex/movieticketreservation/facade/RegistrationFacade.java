package com.acmeplex.movieticketreservation.facade;

import com.acmeplex.movieticketreservation.dto.request.CreateUserRequest;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.services.RegistrationPostProcessingService;
import com.acmeplex.movieticketreservation.services.AcmePlexUserProfileService;
import com.acmeplex.movieticketreservation.services.AddressService;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
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

    public RegistrationFacade(
            AcmePlexUserProfileService userProfileService,
            AddressService addressService,
            PaymentTypeService paymentTypeService,
            RegistrationPostProcessingService postProcessingService) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
        this.postProcessingService = postProcessingService;
    }

    /**
     * Handles the complete registration process.
     *
     * @param createUserRequest      The registration request with user details.
     * @param billingAddressRequest  The billing address details.
     * @param paymentTypeRequest     The payment type details.
     * @return The registered AcmePlexUser.
     */
    @Transactional
    public AcmePlexUser registerUser(CreateUserRequest createUserRequest,
                                     CreateBillingAddressRequest billingAddressRequest,
                                     CreatePaymentTypeRequest paymentTypeRequest) {
        try {
            logger.info("Starting user registration for email: {}", createUserRequest.getEmail());

            // Step 1: Register the user
            AcmePlexUser user = userProfileService.registerUser(createUserRequest);
            logger.debug("User registered successfully: {}", user.getEmail());

            // Step 2: Create billing address
            Address billingAddress = addressService.createBillingAddress(user.getEmail(), billingAddressRequest);
            user.setSavedBillingAddress(billingAddress);
            logger.debug("Billing address saved: {}", billingAddress.getStreet());

            // Step 3: Create payment type
            PaymentType paymentType = paymentTypeService.createPaymentType(user.getEmail(), paymentTypeRequest);
            paymentTypeService.setActivePaymentType(user.getEmail(), paymentType.getId());
            logger.debug("Payment type saved: {}", paymentType.getType());

            // Step 4: Post-registration tasks (e.g., membership creation)
            postProcessingService.handlePostRegistration(user);

            logger.info("Registration process completed successfully for user: {}", createUserRequest.getEmail());
            return user;
        } catch (Exception ex) {
            logger.error("Error during registration workflow for user: {}", createUserRequest.getEmail(), ex);
            throw ex; // Re-throw to be handled by the caller
        }
    }
}
