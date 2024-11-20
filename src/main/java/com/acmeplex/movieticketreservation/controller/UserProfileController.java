package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.SaveBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.SavePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import com.acmeplex.movieticketreservation.services.AcmePlexUserProfileService;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(Constants.Endpoints.API_USER_PROFILE)
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private final AcmePlexUserProfileService userProfileService;
    private final AuthUserPrincipalService authUserPrincipalService;

    public UserProfileController(AcmePlexUserProfileService userProfileService, AuthUserPrincipalService authUserPrincipalService) {
        this.userProfileService = userProfileService;
        this.authUserPrincipalService = authUserPrincipalService;
    }

    private String getAuthenticatedUserEmail() {
        UserPrincipal userPrincipal = authUserPrincipalService.getCurrentUserPrincipal();
        if (userPrincipal == null) {
            throw new IllegalStateException(Constants.Errors.USER_NOT_FOUND);
        }
        return userPrincipal.getUsername();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AcmePlexUser>> getUserProfile() {
        String email = getAuthenticatedUserEmail();
        logger.info("Fetching profile for user: {}", email);
        try {
            AcmePlexUser user = userProfileService.getUserByEmail(email);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED, user));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateProfile(@RequestBody @Validated UpdateProfileRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Updating profile for user: {}", email);
        try {
            userProfileService.updateUserProfile(email, request);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }

    @PostMapping("/billing-address")
    public ResponseEntity<ApiResponse<String>> saveBillingAddress(@RequestBody @Validated SaveBillingAddressRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Saving billing address for user: {}", email);
        try {
            userProfileService.saveBillingAddress(email, request);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }

    @DeleteMapping("/billing-address")
    public ResponseEntity<ApiResponse<String>> removeBillingAddress() {
        String email = getAuthenticatedUserEmail();
        logger.info("Removing billing address for user: {}", email);
        try {
            userProfileService.removeBillingAddress(email);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }

    @PostMapping("/payment-type")
    public ResponseEntity<ApiResponse<String>> savePaymentType(@RequestBody @Validated SavePaymentTypeRequest request) {
        String email = getAuthenticatedUserEmail();
        logger.info("Saving payment type for user: {}", email);
        try {
            userProfileService.savePaymentType(email, request);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }

    @DeleteMapping("/payment-type")
    public ResponseEntity<ApiResponse<String>> removePaymentType() {
        String email = getAuthenticatedUserEmail();
        logger.info("Removing payment type for user: {}", email);
        try {
            userProfileService.removePaymentType(email);
            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.PROFILE_UPDATED));
        } catch (UserNotFoundException e) {
            logger.warn(Constants.Errors.USER_NOT_FOUND + email);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, Constants.Errors.USER_NOT_FOUND + email));
        }
    }
}
