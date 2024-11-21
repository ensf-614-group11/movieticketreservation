package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import com.acmeplex.movieticketreservation.services.AcmePlexUserProfileService;
import com.acmeplex.movieticketreservation.services.AddressService;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Unified Controller for managing user profiles, including billing addresses and payment types.
 */
@RestController
@RequestMapping(Constants.Endpoints.API_USER_PROFILE)
@Validated
public class UserProfileManagementController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileManagementController.class);

    private final AcmePlexUserProfileService userProfileService;
    private final AddressService addressService;
    private final PaymentTypeService paymentTypeService;
    private final AuthUserPrincipalService authUserPrincipalService;

    public UserProfileManagementController(AcmePlexUserProfileService userProfileService,
                                           AddressService addressService,
                                           PaymentTypeService paymentTypeService,
                                           AuthUserPrincipalService authUserPrincipalService) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
        this.authUserPrincipalService = authUserPrincipalService;
    }

    /**
     * Retrieves the authenticated user's email.
     */
    private String getAuthenticatedUserEmail() {
        UserPrincipal userPrincipal = authUserPrincipalService.getCurrentUserPrincipal();
        if (userPrincipal == null) {
            throw new IllegalStateException(Constants.Errors.USER_NOT_FOUND);
        }
        return userPrincipal.getUsername();
    }

    /**
     * Retrieves the user's profile.
     */
    @GetMapping(Constants.Endpoints.API_USER_PROFILE_GET)
    public ResponseEntity<ApiResponse<AcmePlexUser>> getUserProfile() {
        String email = getAuthenticatedUserEmail();
        logger.info("Fetching profile for user: {}", email);
        AcmePlexUser user = userProfileService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED, user));
    }

    /**
     * Updates the user's profile details.
     */
    @PutMapping(Constants.Endpoints.API_USER_PROFILE_UPDATE)
    public ResponseEntity<ApiResponse<AcmePlexUser>> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Updating profile for user: {}", email);
        AcmePlexUser updatedUser = userProfileService.updateUserProfile(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED, updatedUser));
    }

    /**
     * Updates the user's billing address.
     */
    @PutMapping(Constants.Endpoints.API_BILLING_ADDRESS_UPDATE)
    public ResponseEntity<ApiResponse<Address>> updateBillingAddress(
            @RequestBody @Valid UpdateBillingAddressRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Updating billing address for user: {}", email);
        Address updatedAddress = addressService.updateBillingAddress(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.BILLING_ADDRESS_UPDATED, updatedAddress));
    }

    /**
     * Adds a new payment type for the user.
     */
    @PostMapping(Constants.Endpoints.API_PAYMENT_TYPE_CREATE)
    public ResponseEntity<ApiResponse<PaymentType>> addPaymentType(
            @RequestBody @Valid CreatePaymentTypeRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Adding new payment type for user: {}", email);
        PaymentType paymentType = paymentTypeService.createPaymentType(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PAYMENT_TYPE_CREATED, paymentType));
    }

    /**
     * Sets the specified payment type as the active one for the user.
     */
    @PutMapping(Constants.Endpoints.API_PAYMENT_TYPE_SET_ACTIVE)
    public ResponseEntity<ApiResponse<PaymentType>> setActivePaymentType(@RequestParam Long paymentTypeId) {
        String email = getAuthenticatedUserEmail();
        logger.info("Setting payment type ID {} as active for user: {}", paymentTypeId, email);
        PaymentType paymentType = paymentTypeService.setActivePaymentType(email, paymentTypeId);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PAYMENT_TYPE_UPDATED, paymentType));
    }

    /**
     * Retrieves all payment types for the authenticated user.
     */
    @GetMapping(Constants.Endpoints.API_PAYMENT_TYPE_GET)
    public ResponseEntity<ApiResponse<List<PaymentType>>> getAllPaymentTypes() {
        String email = getAuthenticatedUserEmail();
        logger.info("Fetching all payment types for user: {}", email);
        List<PaymentType> paymentTypes = paymentTypeService.getPaymentTypesByUser(email);
        return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PAYMENT_TYPES_FETCHED, paymentTypes));
    }

}
