package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class TheatreRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfSeats;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "theatreRoom", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @OneToMany(mappedBy = "theatreRoom", cascade = CascadeType.ALL)
    private List<Showtime> showtimes;


    public TheatreRoom() {
    }

    public TheatreRoom(int numberOfSeats, Theatre theatre) {
        this.numberOfSeats = numberOfSeats;
        this.theatre = theatre;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;}

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }
    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public Long getId() {
        return id;
    }
}
