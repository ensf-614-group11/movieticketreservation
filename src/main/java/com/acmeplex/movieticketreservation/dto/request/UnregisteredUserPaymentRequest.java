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
}
