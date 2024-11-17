package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Showtime {


    @Id
    @GeneratedValue
    private Long id;
    private Date dateAndTime;

    public Showtime() {
    }

    public Showtime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }


    public Long getId() {
        return id;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
