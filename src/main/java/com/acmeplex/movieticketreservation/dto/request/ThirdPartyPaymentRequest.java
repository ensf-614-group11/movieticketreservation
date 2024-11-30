package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ThirdPartyPaymentRequest {

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

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

    public void setFirstName(@NotBlank(message = "First Name cannot be blank") String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank(message = "Last Name cannot be blank") String lastName) {
        this.lastName = lastName;
    }

    public void setCreditCardNumber(@NotBlank(message = "Credit Card Number cannot be blank") String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setStreet(@NotBlank String street) {
        this.street = street;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    public void setProvince(@NotBlank String province) {
        this.province = province;
    }

    public void setPostalCode(@NotBlank String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(@NotBlank String country) {
        this.country = country;
    }

    public void setCountryCode(@NotBlank String countryCode) {
        this.countryCode = countryCode;
    }

    public void setExpiryDate(@NotBlank String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCvv(@NotBlank String cvv) {
        this.cvv = cvv;
    }
}
