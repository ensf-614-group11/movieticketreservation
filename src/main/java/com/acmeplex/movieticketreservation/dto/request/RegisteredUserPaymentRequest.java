package com.acmeplex.movieticketreservation.dto.request;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import jakarta.validation.constraints.NotBlank;

public class RegisteredUserPaymentRequest extends PaymentRequest{

    @NotBlank(message = "User ID cannot be blank for Registered Users.")
    private Long userId;

    @NotBlank(message = "CVV cannot be blank")
    private String cvv;

    public Long getUserId(){
        return userId;
    }

    public String getCvv() {
        return cvv;
    }
}
