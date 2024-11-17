package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TheatreRoom {

    @Id
    @GeneratedValue
    private Long id;

    private int numberOfSeats;


    public TheatreRoom() {
    }

    public TheatreRoom(int numberOfSeats){
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {}


    public Long getId() {
        return id;
    }
}
