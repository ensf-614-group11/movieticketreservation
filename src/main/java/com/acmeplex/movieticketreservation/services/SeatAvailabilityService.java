package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.SeatAvailability;
import com.acmeplex.movieticketreservation.repository.SeatAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatAvailabilityService {
  private final SeatAvailabilityRepository seatAvailabilityRepository;

  public SeatAvailabilityService(SeatAvailabilityRepository seatAvailabilityRepository) {
    this.seatAvailabilityRepository = seatAvailabilityRepository;
  }

  /**
   * Retrieves a list of all seats for a given showtime ID, including availability
   * status.
   *
   * @param showtimeId the ID of the showtime
   * @return a list of SeatAvailability objects, including available and
   *         unavailable seats
   */
  public List<SeatAvailability> getAllSeats(Long showtimeId) {
    return seatAvailabilityRepository.findAllSeatsByShowtime(showtimeId);
  }
}
