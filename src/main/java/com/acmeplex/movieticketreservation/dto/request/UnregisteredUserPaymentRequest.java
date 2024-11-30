package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UnregisteredUserPaymentRequest extends PaymentRequest{

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;

    @NotBlank(message = "Credit Card Number cannot be blank")
    private String creditCardNumber;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String province;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String expiryDate;

    @NotBlank
    private String cvv;


    public @NotBlank(message = "First Name cannot be blank") String getFirstName() {
        return firstName;
    }

    public @NotBlank(message = "Last Name cannot be blank") String getLastName() {
        return lastName;
    }

    public @NotBlank(message = "Email cannot be blank") @Email String getEmail() {
        return email;
    }

    public @NotBlank(message = "Credit Card Number cannot be blank") String getCreditCardNumber() {
        return creditCardNumber;
    }

    public @NotBlank String getStreet() {
        return street;
    }

    public @NotBlank String getCity() {
        return city;
    }

    public @NotBlank String getProvince() {
        return province;
    }

    public @NotBlank String getPostalCode() {
        return postalCode;
    }

    public @NotBlank String getCountry() {
        return country;
    }

    public @NotBlank String getCountryCode() {
        return countryCode;
    }

    public @NotBlank String getExpiryDate() {
        return expiryDate;
    }

    public @NotBlank String getCvv() {
        return cvv;
    }
}
