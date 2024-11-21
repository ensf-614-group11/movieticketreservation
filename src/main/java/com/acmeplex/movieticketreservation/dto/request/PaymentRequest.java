package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PaymentRequest {

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;

    @NotBlank(message = "Credit Card Number cannot be blank")
    private String creditCardNumber;

    private String creditID; //Credit code for discount

    private float amount;

    private Long seatId;

    private Long showtimeId;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public @NotBlank(message = "First Name cannot be blank") String getFirstName() {
        return firstName;
    }

    public @NotBlank(message = "Last Name cannot be blank") String getLastName() {
        return lastName;
    }

    public @NotBlank(message = "Email cannot be blank") @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be blank") @Email String email) {
        this.email = email;
    }

    public @NotBlank(message = "Credit Card Number cannot be blank") String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditID() {
        return creditID;
    }

    public float getAmount(){
        return amount;
    }



}
