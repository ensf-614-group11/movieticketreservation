package com.acmeplex.movieticketreservation.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Credit {

    @Id
    @GeneratedValue
    private Long id;
    private String creditSecretCode;
    private float amount;

    public Credit(String creditSecretCode, float amount) {
        this.creditSecretCode = creditSecretCode;
        this.amount = amount;
    }

    public Credit() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
