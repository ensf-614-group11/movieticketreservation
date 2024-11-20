package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.Showtime;
import com.acmeplex.movieticketreservation.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
  public List<Showtime> getShowtimes(String date, Long movieId, Long theatreId) {
    return showtimeRepository.findByDateMovieAndTheatre(date, movieId, theatreId);
  }
}
