package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data transfer object for user registration requests.
 */
public class RegisterRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private SavePaymentTypeRequest paymentType; // Optional field for payment type
    private SaveBillingAddressRequest billingAddress; // Optional field for billing address

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SavePaymentTypeRequest getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(SavePaymentTypeRequest paymentType) {
        this.paymentType = paymentType;
    }

    public SaveBillingAddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(SaveBillingAddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }
}
