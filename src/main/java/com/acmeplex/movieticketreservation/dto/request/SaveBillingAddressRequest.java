package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SaveBillingAddressRequest {

    @NotBlank(message = "Street address cannot be blank")
    @Size(max = 100, message = "Street address must not exceed 100 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @NotBlank(message = "Province cannot be blank")
    @Size(max = 50, message = "Province must not exceed 50 characters")
    private String province;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    private String postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    // Getters and setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
