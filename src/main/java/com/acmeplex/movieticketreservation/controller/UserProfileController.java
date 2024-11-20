package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.SaveBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.SavePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.factory.PaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.services.UserBillingAddressService;
import com.acmeplex.movieticketreservation.services.UserPaymentTypeService;
import com.acmeplex.movieticketreservation.services.AcmeUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private final AcmeUserDetailsService userDetailsService;
    private final UserBillingAddressService billingAddressService;
    private final UserPaymentTypeService paymentTypeService;
    private final PaymentTypeFactory paymentTypeFactory;

    public UserProfileController(AcmeUserDetailsService userDetailsService,
                                 UserBillingAddressService billingAddressService,
                                 UserPaymentTypeService paymentTypeService,
                                 PaymentTypeFactory paymentTypeFactory) {
        this.userDetailsService = userDetailsService;
        this.billingAddressService = billingAddressService;
        this.paymentTypeService = paymentTypeService;
        this.paymentTypeFactory = paymentTypeFactory;
    }

    /**
     * Fetches the profile of the currently logged-in user.
     *
     * @param authentication the authentication token of the logged-in user
     * @return ResponseEntity containing the user's profile details
     */
    @GetMapping
    public ResponseEntity<ApiResponse<AcmePlexUser>> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        try {
            AcmePlexUser user = userDetailsService.getUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            logger.info("Fetched profile for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile fetched successfully", user));
        } catch (IllegalArgumentException e) {
            logger.warn("Error fetching profile for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error fetching profile for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Updates the profile information of the currently logged-in user.
     *
     * @param authentication      the authentication token of the logged-in user
     * @param updateProfileRequest the request containing updated profile details
     * @return ResponseEntity indicating whether the update was successful
     */
    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateUserProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest updateProfileRequest) {
        String email = authentication.getName();
        try {
            userDetailsService.updateUserProfile(email, updateProfileRequest);
            logger.info("Profile updated successfully for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Error updating profile for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating profile for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Saves or updates the billing address for the logged-in user.
     *
     * @param authentication the authentication token of the logged-in user
     * @param addressRequest the billing address details
     * @return ResponseEntity indicating whether the operation was successful
     */
    @PostMapping("/billing-address")
    public ResponseEntity<ApiResponse<String>> saveBillingAddress(Authentication authentication, @RequestBody @Validated SaveBillingAddressRequest addressRequest) {
        String email = authentication.getName();
        try {
            AcmePlexUser user = userDetailsService.getUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Address address = new Address();
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setProvince(addressRequest.getProvince());
            address.setPostalCode(addressRequest.getPostalCode());

            billingAddressService.saveBillingAddress(user.getId(), address);
            logger.info("Billing address saved for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Billing address saved successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Error saving billing address for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error saving billing address for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Removes the billing address associated with the logged-in user.
     *
     * @param authentication the authentication token of the logged-in user
     * @return ResponseEntity indicating whether the operation was successful
     */
    @DeleteMapping("/billing-address")
    public ResponseEntity<ApiResponse<String>> removeBillingAddress(Authentication authentication) {
        String email = authentication.getName();
        try {
            AcmePlexUser user = userDetailsService.getUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            billingAddressService.removeBillingAddress(user.getId());
            logger.info("Billing address removed for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Billing address removed successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Error removing billing address for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error removing billing address for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Saves or updates the payment type for the logged-in user.
     *
     * @param authentication      the authentication token of the logged-in user
     * @param paymentTypeRequest the payment type details
     * @return ResponseEntity indicating whether the operation was successful
     */
    @PostMapping("/payment-type")
    public ResponseEntity<ApiResponse<String>> savePaymentType(Authentication authentication, @RequestBody @Validated SavePaymentTypeRequest paymentTypeRequest) {
        String email = authentication.getName();
        try {
            AcmePlexUser user = userDetailsService.getUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            PaymentType paymentType = paymentTypeFactory.createPaymentType(paymentTypeRequest);
            paymentType.setAcmePlexUser(user);

            paymentTypeService.savePaymentType(user.getId(), paymentType);
            logger.info("Payment type saved for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Payment type saved successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Error saving payment type for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error saving payment type for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Removes the payment type associated with the logged-in user.
     *
     * @param authentication the authentication token of the logged-in user
     * @return ResponseEntity indicating whether the operation was successful
     */
    @DeleteMapping("/payment-type")
    public ResponseEntity<ApiResponse<String>> removePaymentType(Authentication authentication) {
        String email = authentication.getName();
        try {
            AcmePlexUser user = userDetailsService.getUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            paymentTypeService.removePaymentType(user.getId());
            logger.info("Payment type removed for user: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Payment type removed successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Error removing payment type for user: {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error removing payment type for user: {}", email, e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Internal server error"));
        }
    }
}
