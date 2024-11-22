package com.acmeplex.movieticketreservation.dto.response;

public class TheatreResponse {
  private Long id;
  private String name;
  private String location;

  public TheatreResponse(Long id, String name, String location) {
    this.id = id;
    this.name = name;
    this.location = location;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
