package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateAndTime;
    private boolean isEarlyRelease;

    @ManyToOne
    @JoinColumn(name = "theatre_room_id")
    @JsonIgnore
    private TheatreRoom theatreRoom;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ticket> tickets;

    public Showtime() {
    }

    public Showtime(LocalDateTime dateAndTime, boolean isEarlyRelease, TheatreRoom theatreRoom, Movie movie) {
        this.dateAndTime = dateAndTime;
        this.isEarlyRelease = isEarlyRelease;
        this.theatreRoom = theatreRoom;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public boolean isEarlyRelease() {
        return isEarlyRelease;
    }

    public void setEarlyRelease(boolean earlyRelease) {
        isEarlyRelease = earlyRelease;
    }

    public TheatreRoom getTheatreRoom() {
        return theatreRoom;
    }

    public void setTheatreRoom(TheatreRoom theatreRoom) {
        this.theatreRoom = theatreRoom;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}
