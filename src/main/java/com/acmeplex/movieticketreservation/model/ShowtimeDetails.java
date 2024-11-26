package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

@JsonSerialize
public class ShowtimeDetails {
    LocalDateTime dateAndTime;
    String movieTitle;
    String theatre;

    public ShowtimeDetails(Showtime showtime) {
        this.dateAndTime = showtime.getDateAndTime();
        this.movieTitle = showtime.getMovie() != null ? showtime.getMovie().getTitle() : null;
        this.theatre = showtime.getTheatreRoom().getTheatre().getName();

    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getTheatre() {
        return theatre;
    }


}
