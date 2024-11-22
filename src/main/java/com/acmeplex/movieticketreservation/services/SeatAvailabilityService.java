package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.SeatAvailabilityResponse;
import com.acmeplex.movieticketreservation.model.SeatAvailability;
import com.acmeplex.movieticketreservation.repository.SeatAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
  public List<SeatAvailabilityResponse> getAllSeats(Long showtimeId) {
    List<SeatAvailability> seatAvailabilities = seatAvailabilityRepository.findAllSeatsByShowtime(showtimeId);

    return seatAvailabilities.stream()
        .map(sa -> new SeatAvailabilityResponse(sa.getSeat().getId(), sa.isAvailable()))
        .collect(Collectors.toList());
  }
}
