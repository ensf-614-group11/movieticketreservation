package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface SeatAvailabilityRepository extends JpaRepository<SeatAvailability, Integer> {
    Optional<SeatAvailability> findBySeatId(Long seatId);
  /**
   * Finds all seats (whether available or not) for a specific showtime.
   *
   * @param showtimeId the ID of the showtime
   * @return a list of SeatAvailability objects
   */
  @Query("""
          SELECT sa
          FROM SeatAvailability sa
          WHERE sa.showtime.id = :showtimeId
      """)
  List<SeatAvailability> findAllSeatsByShowtime(@Param("showtimeId") Long showtimeId);

}
