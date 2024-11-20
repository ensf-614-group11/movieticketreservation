package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.services.TheatreService;
import com.acmeplex.movieticketreservation.services.SeatAvailabilityService;
import com.acmeplex.movieticketreservation.services.ShowtimeService;
import com.acmeplex.movieticketreservation.model.Movie;
import com.acmeplex.movieticketreservation.model.SeatAvailability;
import com.acmeplex.movieticketreservation.model.Showtime;
import com.acmeplex.movieticketreservation.model.Theatre;
import com.acmeplex.movieticketreservation.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/theatres")
public class TheatreController {

  private final MovieService movieService;
  private final TheatreService theatreService;
  private final ShowtimeService showtimeService;
  private final SeatAvailabilityService seatAvailabilityService;

  public TheatreController(MovieService movieService, TheatreService theatreService, ShowtimeService showtimeService,
      SeatAvailabilityService seatAvailabilityService) {
    this.movieService = movieService;
    this.theatreService = theatreService;
    this.showtimeService = showtimeService;
    this.seatAvailabilityService = seatAvailabilityService;
  }

  /**
   * Endpoint to fetch all movies from the system.
   * <p>
   * This method retrieves a list of all movies available in the system, which
   * includes movie details such as titles, descriptions, and other relevant
   * information.
   * </p>
   *
   * @return a list of all movies
   */
  @GetMapping("/movies")
  public List<Movie> getMovies() {
    return movieService.getAllMovies();
  }

  /**
   * Endpoint to fetch all theatres from the system.
   *
   * @return a list of all theatres
   */

  @GetMapping
  public List<Theatre> getTheatres() {
    return theatreService.getAllTheatres();
  }

  /**
   * Endpoint to fetch all showtimes for a specific movie at a specific theatre
   * on a given date.
   *
   * @param date      the date for which showtimes are being requested
   * @param movieId   the ID of the movie
   * @param theatreId the ID of the theatre
   * @return a list of showtimes for the requested date, movie, and theatre
   */
  @GetMapping("/showtimes")
  public List<Showtime> getShowtimes(
      @RequestParam String date,
      @RequestParam Long movieId,
      @RequestParam Long theatreId) {
    return showtimeService.getShowtimes(date, movieId, theatreId);
  }

  /**
   * Endpoint to fetch all seats for a specific showtime, including availability
   * status.
   *
   * @param showtimeId the ID of the showtime
   * @return a list of all seats for the showtime with availability status
   */
  @GetMapping("/seats/all")
  public List<SeatAvailability> getAllSeats(@RequestParam Long showtimeId) {
    return seatAvailabilityService.getAllSeats(showtimeId);
  }
}
