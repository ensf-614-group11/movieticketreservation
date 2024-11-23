package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        Movie avengers = new Movie("Avengers", 2.3, "Tony Stark woops butt.");
        Movie ultron = new Movie("Avengers: Age of Ultron", 2.5, "Ultron and Vision duke it out.");
        Movie infinityWar = new Movie("Avengers: Infinity War", 3, "Thanos' Guide to the Galaxy.");
        Movie endgame = new Movie("Avengers: Endgame", 3.2, "Time Travelies.");
        movieRepository.saveAll(List.of(avengers, ultron, infinityWar, endgame));
        logger.info("Movies have been added to the database.");
    }

    private void initializeTheatresAndSeats() {
        Theatre crowfootTheatre = theatreRepository.save(new Theatre("800 Crowfoot Street SE Edmonton AB"));

        TheatreRoom room1 = new TheatreRoom(108, crowfootTheatre);
        TheatreRoom room2 = new TheatreRoom(150, crowfootTheatre);
        TheatreRoom room3 = new TheatreRoom(120, crowfootTheatre);
        theatreRoomRepository.saveAll(List.of(room1, room2, room3));

        addSeatsToRoom(room1, 108, 12);
        addSeatsToRoom(room2, 150, 15);
        addSeatsToRoom(room3, 120, 10);

        logger.info("Theatres and seats have been initialized.");
    }

    private void addSeatsToRoom(TheatreRoom room, int totalSeats, int seatsPerRow) {
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat(i / seatsPerRow + 1, i % seatsPerRow == 0 ? seatsPerRow : i % seatsPerRow, room);
            seatRepository.save(seat);
        }
    }

    private void initializeShowtimes() {
        TheatreRoom room1 = theatreRoomRepository.findAll().get(0); // Example room
        List<Movie> movies = movieRepository.findAll();

        showtimeRepository.save(new Showtime(new Date(), false, room1, movies.get(0))); // Avengers
        showtimeRepository.save(new Showtime(new Date(), true, room1, movies.get(1))); // Age of Ultron
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
