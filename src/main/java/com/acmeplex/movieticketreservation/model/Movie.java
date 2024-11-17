package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int length;
    private String description;


    public Movie() {
    }

    public Movie(String title, int length, String description) {
        this.title = title;
        this.length = length;
        this.description = description;

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
