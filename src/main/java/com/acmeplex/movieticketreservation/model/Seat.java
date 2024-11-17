package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Seat {

    @Id
    @GeneratedValue
    private Long id;
    private int rowNumber;
    private int seatNumber;
    private String status; //available or unavailable

    public Seat(int rowNumber, int seatNumber, String status) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Seat() {

    }


    public Long getId() {
        return id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status = status; }
}
