package com.acmeplex.movieticketreservation.dto.response;

public class ThirdPartyPaymentResponse {

    private String status;
    private String message;

    // Constructor
    public ThirdPartyPaymentResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
