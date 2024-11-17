package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private Long id;


    public Receipt() {

    }


    public Long getId() {
        return id;
    }


}
