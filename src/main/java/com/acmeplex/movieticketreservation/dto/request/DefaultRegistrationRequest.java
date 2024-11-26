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
    private BillingAddressRequest billingAddress;

    @NotNull(message = "Payment type details are required.")
    @Valid
    private PaymentTypeRequest paymentType;

    // Default constructor
    public DefaultRegistrationRequest() {
    }

    // Constructor for initialization
    public DefaultRegistrationRequest(CreateUserRequest user,
                                      BillingAddressRequest billingAddress,
                                      PaymentTypeRequest paymentType) {
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

    public BillingAddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }

    public PaymentTypeRequest getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeRequest paymentType) {
        this.paymentType = paymentType;
    }
}