package com.acmeplex.movieticketreservation.dto.response;

import java.time.LocalDateTime;

public class ShowtimeResponse {
  private Long id;
  private LocalDateTime dateAndTime;
  private String movieTitle;
  private String theatreName;
  private boolean isEarlyRelease;

  public ShowtimeResponse(Long id, LocalDateTime dateAndTime, String movieTitle, String theatreName,
      boolean isEarlyRelease) {
    this.id = id;
    this.dateAndTime = dateAndTime;
    this.movieTitle = movieTitle;
    this.theatreName = theatreName;
    this.isEarlyRelease = isEarlyRelease;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getDateAndTime() {
    return dateAndTime;
  }

  public String getMovieTitle() {
    return movieTitle;
  }

  public String getTheatreName() {
    return theatreName;
  }

  public boolean isEarlyRelease() {
    return isEarlyRelease;
  }
}
