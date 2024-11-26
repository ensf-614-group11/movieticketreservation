package com.acmeplex.movieticketreservation.dto.request;

import com.acmeplex.movieticketreservation.model.CountryCode;
import com.acmeplex.movieticketreservation.validation.annotation.ValidCountryCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data transfer object for user registration requests.
 */
public class CreateUserRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @ValidCountryCode
    @NotBlank(message = "Country code is required.")
    private String countryCode;

    private PaymentTypeRequest paymentType; // Optional field for payment type
    private BillingAddressRequest billingAddress; // Optional field for billing address

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

    public String getFirstName() { return firstName; }

    public String getCountryCode() { return countryCode; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PaymentTypeRequest getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeRequest paymentType) {
        this.paymentType = paymentType;
    }

    public BillingAddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Converts the countryCode string into a CountryCode enum.
     * Uses the CountryCode model's conversion logic.
     *
     * @return CountryCode
     */
    public CountryCode getCountryCodeEnum() {
        return CountryCode.fromString(countryCode);
    }


}