package com.acmeplex.movieticketreservation.dto.response;

import java.sql.Date;

public class MovieResponse {
  private Long id;
  private String title;
  private int length;
  private Date releaseDate;
  private String description;

  public MovieResponse(Long id, String title, int length, Date releaseDate, String description) {
    this.id = id;
    this.title = title;
    this.length = length;
    this.releaseDate = releaseDate;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
