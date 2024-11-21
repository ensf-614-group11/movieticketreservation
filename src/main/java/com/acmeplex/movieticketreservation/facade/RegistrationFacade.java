package com.acmeplex.movieticketreservation.facade;

import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
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

    public RegistrationFacade(
            AcmePlexUserProfileService userProfileService,
            AddressService addressService,
            PaymentTypeService paymentTypeService) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
    }

    /**
     * Handles the complete registration process.
     *
     * @param registerRequest        The registration request with user details.
     * @param billingAddressRequest  The billing address details.
     * @param paymentTypeRequest     The payment type details.
     * @return The registered AcmePlexUser.
     */
    @Transactional
    public AcmePlexUser registerUser(RegisterRequest registerRequest,
                                     CreateBillingAddressRequest billingAddressRequest,
                                     CreatePaymentTypeRequest paymentTypeRequest) {

        logger.info("Starting registration process for user: {}", registerRequest.getEmail());

        // Step 1: Register the user
        AcmePlexUser user = userProfileService.registerUser(registerRequest);

        // Step 2: Validate and save billing address
        Address billingAddress = addressService.createBillingAddress(user.getEmail(), billingAddressRequest);
        user.setSavedBillingAddress(billingAddress);

        // Step 3: Validate and save the first payment type
        PaymentType paymentType = paymentTypeService.createPaymentType(user.getEmail(), paymentTypeRequest);

        // Step 4: Automatically set the first payment type as active
        paymentTypeService.setActivePaymentType(user.getEmail(), paymentType.getId());

        logger.info("Registration process completed successfully for user: {}", registerRequest.getEmail());
        return user;
    }
}

