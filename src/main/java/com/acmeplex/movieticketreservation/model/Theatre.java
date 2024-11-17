package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Theatre {

    @Id
    @GeneratedValue
    private Long id;

    private String location;

    public Theatre() {}


    public Theatre(String location) {
        this.location = location;

    }

    public String getLocation(){
        return location; 
    }

    public void setLocation(String location){
        this.location = location; 
    }

    public Long getId() {
        return id;
    }
}
