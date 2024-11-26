package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.BillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.dto.response.*;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.services.AcmePlexUserProfileService;
import com.acmeplex.movieticketreservation.services.AddressService;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/profile")
@Validated
public class AcmePlexUserProfileManagement {

    private final AcmePlexUserProfileService userProfileService;
    private final AddressService addressService;
    private final PaymentTypeService paymentTypeService;
    private final AuthUserPrincipalService authUserPrincipalService;

    public AcmePlexUserProfileManagement(AcmePlexUserProfileService userProfileService,
                                         AddressService addressService,
                                         PaymentTypeService paymentTypeService,
                                         AuthUserPrincipalService authUserPrincipalService) {
        this.userProfileService = userProfileService;
        this.addressService = addressService;
        this.paymentTypeService = paymentTypeService;
        this.authUserPrincipalService = authUserPrincipalService;
    }

    private String getAuthenticatedUserEmail() {
        return authUserPrincipalService.getCurrentUserPrincipal().getUsername();
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<AcmePlexUser>> getUserProfile() {
        String email = getAuthenticatedUserEmail();
        AcmePlexUser user = userProfileService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", user));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<AcmePlexUser>> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        String email = getAuthenticatedUserEmail();
        AcmePlexUser updatedUser = userProfileService.updateUserProfile(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully"));
    }

    @PutMapping("/billing-address")
    public ResponseEntity<ApiResponse<Address>> updateBillingAddress(@RequestBody @Valid BillingAddressRequest request) {
        String email = getAuthenticatedUserEmail();
        Address updatedAddress = addressService.updateBillingAddress(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Billing address updated successfully"));
    }

    @PostMapping("/payment-type")
    public ResponseEntity<ApiResponse<PaymentType>> addPaymentType(@RequestBody @Valid PaymentTypeRequest request) {
        String email = getAuthenticatedUserEmail();
        PaymentType paymentType = paymentTypeService.createPaymentType(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment type created successfully"));
    }

    @PutMapping("/payment-type/set-active")
    public ResponseEntity<ApiResponse<PaymentType>> setActivePaymentType(@RequestParam Long paymentTypeId) {
        String email = getAuthenticatedUserEmail();
        PaymentType paymentType = paymentTypeService.setActivePaymentType(email, paymentTypeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment type updated successfully", paymentType));
    }

    @GetMapping("/payment-type/list")
    public ResponseEntity<ApiResponse<List<PaymentType>>> getAllPaymentTypes() {
        String email = getAuthenticatedUserEmail();
        List<PaymentType> paymentTypes = paymentTypeService.getPaymentTypesByUser(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "User payment types retrieved successfully", paymentTypes));
    }

    @PutMapping("/payment-type/manage")
    public ResponseEntity<ApiResponse<PaymentType>> managePaymentType(@RequestBody @Valid PaymentTypeRequest request) {
        String email = getAuthenticatedUserEmail();
        PaymentType paymentType = paymentTypeService.updateOrCreateAndSetActive(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment type updated successfully", paymentType));
    }
}
