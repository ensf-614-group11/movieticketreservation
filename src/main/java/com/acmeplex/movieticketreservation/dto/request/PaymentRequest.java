package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public abstract class PaymentRequest {

    private float amount;
    private Long seatId;
    private Long showtimeId;

    public Long getSeatId() {
        return seatId;
    }

    public float getAmount() {
        return amount;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

}
