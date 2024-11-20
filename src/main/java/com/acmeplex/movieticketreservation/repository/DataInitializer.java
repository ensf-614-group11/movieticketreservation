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
    private final AcmePlexCreditRepository creditRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;
    private final SeatRepository seatRepository;
    private final SeatAvailabilityRepository seatAvailabilityRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TheatreRepository theatreRepository;
    private final TheatreRoomRepository theatreRoomRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MovieRepository movieRepository, AcmePlexCreditRepository creditRepository,
                           UserRepository userRepository, NotificationRepository notificationRepository,
                           PaymentRepository paymentRepository, ReceiptRepository receiptRepository,
                           SeatRepository seatRepository, SeatAvailabilityRepository seatAvailabilityRepository,
                           ShowtimeRepository showtimeRepository,
                           TheatreRepository theatreRepository, TheatreRoomRepository theatreRoomRepository,
                           TicketRepository ticketRepository, PasswordEncoder passwordEncoder) {
        this.movieRepository = movieRepository;
        this.creditRepository = creditRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
        this.seatRepository = seatRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
        this.showtimeRepository = showtimeRepository;
        this.theatreRepository = theatreRepository;
        this.theatreRoomRepository = theatreRoomRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        try {
            //Users
            AcmePlexUser user1 = new AcmePlexUser("admin@example.com", passwordEncoder.encode("1111"), "Admin", "User");
            AcmePlexUser user2 = new AcmePlexUser("user@example.com", passwordEncoder.encode("1111"), "Regular", "User");
            userRepository.save(user1);
            userRepository.save(user2);

            logger.info("Sample users have been added to the database.");

            // Movies
            Movie avengers = movieRepository.save(new Movie("Avengers", 2.3, "Tony Stark woops butt."));
            Movie ultron = movieRepository.save(new Movie("Avengers: Age of Ultron", 2.5, "Ultron and Vision duke it out."));
            Movie infinityWar = movieRepository.save(new Movie("Avengers: Infinity War", 3, "Thanos' Guide to the Galaxy."));
            Movie endgame = movieRepository.save(new Movie("Avengers: Endgame", 3.2, "Time Travelies."));

            logger.info("Movies have been added to the database.");

            // Theatres
            Theatre crowfootTheatre = theatreRepository.save(new Theatre("800 Crowfoot Street SE Edmonton AB"));

            logger.info("Theatres have been added to the database.");

            // Theatre Rooms
            TheatreRoom crowfootRoom1 = theatreRoomRepository.save(new TheatreRoom(108, crowfootTheatre));
            TheatreRoom crowfootRoom2 = theatreRoomRepository.save(new TheatreRoom(150, crowfootTheatre));
            TheatreRoom crowfootRoom3 = theatreRoomRepository.save(new TheatreRoom(120, crowfootTheatre));

            logger.info("Theatre Rooms have been added to the database.");

            // Seats for Crowfoot Room 1
            for (int i = 1; i <= 108; i++) {
                seatRepository.save(new Seat(i / 12 + 1, i % 12 == 0 ? 12 : i % 12, crowfootRoom1));
            }

            // Seats for Crowfoot Room 2
            for (int i = 1; i <= 150; i++) {
                seatRepository.save(new Seat(i / 15 + 1, i % 15 == 0 ? 15 : i % 15, crowfootRoom2));
            }

            // Seats for Crowfoot Room 3
            for (int i = 1; i <= 120; i++) {
                seatRepository.save(new Seat(i / 10 + 1, i % 10 == 0 ? 10 : i % 10, crowfootRoom3));
            }

            logger.info("Theatre room seats have been added to the database.");

            // Showtimes
            showtimeRepository.save(new Showtime(new Date(), false, crowfootRoom1, avengers));
            showtimeRepository.save(new Showtime(new Date(), true, crowfootRoom1, ultron));
            showtimeRepository.save(new Showtime(new Date(), false, crowfootRoom2, infinityWar));
            showtimeRepository.save(new Showtime(new Date(), true, crowfootRoom3, endgame));

            logger.info("Showtimes have been added to the database.");

            // Initialize Seat Availability for each Showtime
            List<Showtime> allShowtimes = showtimeRepository.findAll();

            for (Showtime showtime : allShowtimes) {
                // Get the TheatreRoom associated with the current Showtime
                TheatreRoom theatreRoom = showtime.getTheatreRoom();

                // Find all seats associated with this TheatreRoom
                List<Seat> seatsInTheatreRoom = seatRepository.findByTheatreRoom(theatreRoom);

                for (Seat seat : seatsInTheatreRoom) {
                    // Create and save SeatAvailability for the seat and showtime
                    SeatAvailability seatAvailability = new SeatAvailability();
                    seatAvailability.setAvailable(true); // Initially, all seats are available
                    seatAvailability.setShowtime(showtime);
                    seatAvailability.setSeat(seat);
                    seatAvailabilityRepository.save(seatAvailability);
                }
            }

            logger.info("Seat availabilities have been initialized for all showtimes and their respective theatre room seats.");


            // Notifications
            notificationRepository.save(new Notification(new Date(), "Your ticket is confirmed", user1));
            notificationRepository.save(new Notification(new Date(), "Your payment is successful", user2));

            logger.info("Notifications have been added to the database.");

            // Tickets
            Seat ticketSeat1 = seatRepository.findByTheatreRoomAndRowNumberAndSeatNumber(crowfootRoom1, 1, 1);
            Showtime ticketShowtime1 = showtimeRepository.findByTheatreRoomAndMovie(crowfootRoom1, avengers);

            Ticket ticket1 = ticketRepository.save(new Ticket("confirmed", new Date(), ticketShowtime1, ticketSeat1, user1));

            logger.info("Tickets have been added to the database.");

            // Payments
            Payment payment1 = paymentRepository.save(new Payment(12.99f, new Date(), "completed", "credit card", ticket1, user1));

            logger.info("Payments have been added to the database.");

            // Receipts
            receiptRepository.save(new Receipt(payment1, user1));

            logger.info("Receipts have been added to the database.");

            // Credits
            creditRepository.save(new AcmePlexCredit("CRED12345", 10.00f, payment1));

            logger.info("Credits have been added to the database.");
        } catch (Exception e) {
            // Log any exceptions that occur during the database initialization
            logger.error("Error occurred while initializing the database: {}", e.getMessage(), e);
        }

    }

}
