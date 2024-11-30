package com.acmeplex.movieticketreservation.dto.response;

public class PaymentResponse {

    private boolean success;
    private String message;
    private Long paymentId;

    public PaymentResponse(boolean success, String message, Long paymentId) {
        this.success = success;
        this.message = message;
        this.paymentId = paymentId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTransactionId() {
        return paymentId;
    }

    public void setTransactionId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
