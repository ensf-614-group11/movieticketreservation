package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * A wrapper DTO for combining registration-related requests.
 */
public class DefaultRegistrationRequest {

    @NotNull(message = "User registration details are required.")
    @Valid
    private CreateUserRequest user;

    @NotNull(message = "Billing address details are required.")
    @Valid
    private CreateBillingAddressRequest billingAddress;

    @NotNull(message = "Payment type details are required.")
    @Valid
    private CreatePaymentTypeRequest paymentType;

    // Default constructor
    public DefaultRegistrationRequest() {
    }

    // Constructor for initialization
    public DefaultRegistrationRequest(CreateUserRequest user,
                                      CreateBillingAddressRequest billingAddress,
                                      CreatePaymentTypeRequest paymentType) {
        this.user = user;
        this.billingAddress = billingAddress;
        this.paymentType = paymentType;
    }

    // Getters and setters
    public CreateUserRequest getUser() {
        return user;
    }

    public void setUser(CreateUserRequest user) {
        this.user = user;
    }

    public CreateBillingAddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(CreateBillingAddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }

    public CreatePaymentTypeRequest getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(CreatePaymentTypeRequest paymentType) {
        this.paymentType = paymentType;
    }
}