package com.acmeplex.movieticketreservation.dto.response;

public class TheatreResponse {
  private Long id;
  private String location;

  public TheatreResponse(Long id, String location) {
    this.id = id;
    this.location = location;
  }

  public Long getId() {
    return id;
  }

  public String getLocation() {
    return location;
  }
}
