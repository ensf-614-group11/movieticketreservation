package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
public class DataInitializer {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  private final MovieRepository movieRepository;
  private final UserRepository userRepository;
  private final SeatRepository seatRepository;
  private final SeatAvailabilityRepository seatAvailabilityRepository;
  private final ShowtimeRepository showtimeRepository;
  private final TheatreRepository theatreRepository;
  private final TheatreRoomRepository theatreRoomRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(MovieRepository movieRepository,
      UserRepository userRepository,
      SeatRepository seatRepository,
      SeatAvailabilityRepository seatAvailabilityRepository,
      ShowtimeRepository showtimeRepository,
      TheatreRepository theatreRepository,
      TheatreRoomRepository theatreRoomRepository,
      PasswordEncoder passwordEncoder) {
    this.movieRepository = movieRepository;
    this.userRepository = userRepository;
    this.seatRepository = seatRepository;
    this.seatAvailabilityRepository = seatAvailabilityRepository;
    this.showtimeRepository = showtimeRepository;
    this.theatreRepository = theatreRepository;
    this.theatreRoomRepository = theatreRoomRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void init() {
    try {
      initializeUsers();
      initializeMovies();
      initializeTheatresAndSeats();
      initializeShowtimes();
      initializeSeatAvailabilities();
    } catch (Exception e) {
      logger.error("Error occurred while initializing the database: {}", e.getMessage(), e);
    }
  }

  private void initializeUsers() {
    AcmePlexUser user1 = new AcmePlexUser("admin@example.com", passwordEncoder.encode("1111"), "Admin", "User");
    user1.activateMembership();
    AcmePlexUser user2 = new AcmePlexUser("user@example.com", passwordEncoder.encode("1111"), "Regular", "User");
    userRepository.saveAll(List.of(user1, user2));
    logger.info("Sample users have been added to the database.");
  }

  private void initializeMovies() {
    // Movies
    Movie avengers = movieRepository
        .save(new Movie("Avengers", 120, java.sql.Date.valueOf("2024-05-26"), "Tony Stark woops butt."));
    Movie ultron = movieRepository
        .save(new Movie("Avengers: Age of Ultron", 130, java.sql.Date.valueOf("2024-09-21"),
            "Ultron and Vision duke it out."));
    Movie infinityWar = movieRepository
        .save(new Movie("Avengers: Infinity War", 110, java.sql.Date.valueOf("2024-10-20"),
            "Thanos' Guide to the Galaxy."));
    Movie endgame = movieRepository
        .save(new Movie("Avengers: Endgame", 140, java.sql.Date.valueOf("2024-11-15"), "Time Travelies."));
    movieRepository.saveAll(List.of(avengers, ultron, infinityWar, endgame));
    logger.info("Movies have been added to the database.");
  }

  private void initializeTheatresAndSeats() {
    // Theatres
    Theatre crowfootTheatre = theatreRepository
        .save(new Theatre("Crowfoot Theatre", "800 Crowfoot Street SE Edmonton AB"));

    TheatreRoom room1 = new TheatreRoom(40, crowfootTheatre);
    TheatreRoom room2 = new TheatreRoom(40, crowfootTheatre);
    TheatreRoom room3 = new TheatreRoom(40, crowfootTheatre);
    theatreRoomRepository.saveAll(List.of(room1, room2, room3));

    addSeatsToRoom(room1, 40, 8);
    addSeatsToRoom(room2, 40, 8);
    addSeatsToRoom(room3, 40, 8);

    logger.info("Theatres and seats have been initialized.");
  }

  private void addSeatsToRoom(TheatreRoom room, int totalSeats, int seatsPerRow) {
    for (int i = 1; i <= totalSeats; i++) {
      int row = (i - 1) / seatsPerRow + 1;
      int column = (i - 1) % seatsPerRow + 1;
      Seat seat = new Seat(row, column, room);
      seatRepository.save(seat);
    }
  }

  private void initializeShowtimes() {
    TheatreRoom room1 = theatreRoomRepository.findAll().get(0); // Example room
    List<Movie> movies = movieRepository.findAll();

    LocalDateTime showtime1 = LocalDateTime.of(2024, Month.NOVEMBER, 25, 14, 30);
    LocalDateTime showtime2 = LocalDateTime.of(2024, Month.NOVEMBER, 25, 17, 00);
    LocalDateTime showtime3 = LocalDateTime.of(2024, Month.NOVEMBER, 26, 19, 30);
    LocalDateTime showtime4 = LocalDateTime.of(2024, Month.NOVEMBER, 26, 21, 15);

    showtimeRepository.save(new Showtime(showtime1, false, room1, movies.get(0))); // Avengers
    showtimeRepository.save(new Showtime(showtime2, true, room1, movies.get(1))); // Age of Ultron
    showtimeRepository.save(new Showtime(showtime3, false, room1, movies.get(0))); // Avenger
    showtimeRepository.save(new Showtime(showtime4, true, room1, movies.get(1))); // Age of Ultron
    logger.info("Showtimes have been added to the database.");
  }

  private void initializeSeatAvailabilities() {
    List<Showtime> showtimes = showtimeRepository.findAll();

    for (Showtime showtime : showtimes) {
      TheatreRoom room = showtime.getTheatreRoom();
      List<Seat> seats = seatRepository.findByTheatreRoom(room);

      for (Seat seat : seats) {
        SeatAvailability availability = new SeatAvailability();
        availability.setShowtime(showtime);
        availability.setSeat(seat);
        availability.setAvailable(true);
        seatAvailabilityRepository.save(availability);
      }
    }
    logger.info("Seat availabilities have been initialized.");
  }
}
