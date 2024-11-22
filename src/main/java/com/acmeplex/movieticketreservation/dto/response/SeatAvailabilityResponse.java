package com.acmeplex.movieticketreservation.dto.response;

public class SeatAvailabilityResponse {
  private Long seatId;
  private boolean isAvailable;

  public SeatAvailabilityResponse(Long seatId, boolean isAvailable) {
    this.seatId = seatId;
    this.isAvailable = isAvailable;
  }

  public Long getSeatId() {
    return seatId;
  }

  public void setSeatId(Long seatId) {
    this.seatId = seatId;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }
}
