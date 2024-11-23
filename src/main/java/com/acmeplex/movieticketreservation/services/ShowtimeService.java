package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.ShowtimeResponse;
import com.acmeplex.movieticketreservation.model.Showtime;
import com.acmeplex.movieticketreservation.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {
  private final ShowtimeRepository showtimeRepository;

  public ShowtimeService(ShowtimeRepository showtimeRepository) {
    this.showtimeRepository = showtimeRepository;
  }

  /**
   * Retrieves showtimes filtered by date, movie ID, and theatre ID.
   *
   * @param date      the date for the showtimes (in YYYY-MM-DD format)
   * @param movieId   the ID of the movie
   * @param theatreId the ID of the theatre
   * @return a list of showtimes matching the criteria
   */
  public List<ShowtimeResponse> getShowtimesResponse(String date, Long movieId, Long theatreId) {
    LocalDate localDate = LocalDate.parse(date);
    LocalDateTime startOfDay = localDate.atStartOfDay();
    LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

    List<Showtime> showtimes = showtimeRepository.findByDateMovieAndTheatre(startOfDay, endOfDay, movieId, theatreId);

    return showtimes.stream()
        .map(showtime -> new ShowtimeResponse(
            showtime.getId(),
            showtime.getDateAndTime(),
            showtime.getMovie().getTitle(),
            showtime.getTheatreRoom().getTheatre().getName(),
            showtime.isEarlyRelease()))
        .collect(Collectors.toList());
  }
}
