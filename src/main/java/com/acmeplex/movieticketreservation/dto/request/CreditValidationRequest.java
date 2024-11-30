package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreditValidationRequest {

    @NotBlank(message = "Credit secret code cannot be blank")
    private String creditSecretCode;

    @NotBlank(message = "Amount cannot be blank.")
    private float amount;

    public @NotBlank(message = "Credit secret code cannot be blank") String getCreditSecretCode() {
        return creditSecretCode;
    }

    public void setCreditSecretCode(@NotBlank(message = "Credit secret code cannot be blank") String creditSecretCode) {
        this.creditSecretCode = creditSecretCode;
    }

    @NotBlank(message = "Amount cannot be blank.")
    public float getAmount() {
        return amount;
    }

    public void setAmount(@NotBlank(message = "Amount cannot be blank.") float amount) {
        this.amount = amount;
    }
}
