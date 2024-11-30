package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                           ShowtimeRepository showtimeRepository, TheatreRepository theatreRepository,
                           TheatreRoomRepository theatreRoomRepository, TicketRepository ticketRepository,
                           PasswordEncoder passwordEncoder) {
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
            initializeUsers();
            initializePaymentTypes();
            initializeMovies();
            initializeTheatresAndSeats();
            initializeShowtimes();
            initializeSeatAvailabilities();
            //initializeTicketsPaymentsAndReceipts();
        } catch (Exception e) {
            logger.error("Error occurred while initializing the database: {}", e.getMessage(), e);
        }
    }

    private void initializeUsers() {
        // Create memberships for users
        AcmePlexMembership membership1 = new AcmePlexMembership();
        AcmePlexMembership membership2 = new AcmePlexMembership();

        // Create billing addresses
        Address address1 = new Address(
                "123 Main Street",
                "New York",
                "NY",
                "10001",
                "US"
        );

        Address address2 = new Address(
                "456 Maple Avenue",
                "Toronto",
                "ON",
                "M4B 1B3",
                "CA"
        );

        // Create users
        AcmePlexUser user1 = new AcmePlexUser("admin@example.com", passwordEncoder.encode("admin123"), "Admin", "User", CountryCode.US);
        user1.setMembership(membership1);
        user1.setSavedBillingAddress(address1); // Associate billing address
        membership1.setAcmePlexUser(user1);

        AcmePlexUser user2 = new AcmePlexUser("user@example.com", passwordEncoder.encode("user123"), "Regular", "User", CountryCode.CA);
        user2.setMembership(membership2);
        user2.setSavedBillingAddress(address2); // Associate billing address
        membership2.setAcmePlexUser(user2);

        // Save users and memberships
        userRepository.saveAll(List.of(user1, user2));
        logger.info("Users, memberships, and billing addresses have been initialized.");
    }


    private void initializePaymentTypes() {
        List<AcmePlexUser> users = userRepository.findAll();

        for (AcmePlexUser user : users) {
            if (user.getEmail() != null) { // Ensure payment types are only added for non-guest users
                CreditCard creditCard = new CreditCard(
                        0.0f, // Initial amount, since this is just a payment type definition
                        "Active",
                        "CREDIT_CARD",
                        user,
                        "4111111111111111", // Example test card number
                        user.getFirstName() + " " + user.getLastName(), // Cardholder name
                        "12/26" // Expiry date
                );

                user.setPaymentTypes(List.of(creditCard));
            }
        }

        userRepository.saveAll(users);
        logger.info("Payment types (Credit Cards) have been added to all users.");
    }

    private void initializeMovies() {
        Movie avengers = new Movie("Avengers", 138, Date.valueOf("2012-04-25"), "Tony Stark woops butt.");
        Movie ultron = new Movie("Avengers: Age of Ultron", 150, Date.valueOf("2015-04-23"), "Ultron and Vision duke it out.");
        Movie infinityWar = new Movie("Avengers: Infinity War", 180, Date.valueOf("2018-04-27"), "Thanos' Guide to the Galaxy.");
        Movie endgame = new Movie("Avengers: Endgame", 192, Date.valueOf("2019-04-26"), "Time Travelies.");

        movieRepository.saveAll(List.of(avengers, ultron, infinityWar, endgame));
        logger.info("Movies have been initialized.");
    }

    private void initializeTheatresAndSeats() {
        Theatre crowfootTheatre = new Theatre("Crowfoot Theatre", "800 Crowfoot Street SE Edmonton AB");
        theatreRepository.save(crowfootTheatre);

        TheatreRoom room1 = new TheatreRoom(108, crowfootTheatre);
        TheatreRoom room2 = new TheatreRoom(150, crowfootTheatre);

        theatreRoomRepository.saveAll(List.of(room1, room2));

        for (int i = 1; i <= 108; i++) {
            Seat seat = new Seat(i / 12 + 1, i % 12 == 0 ? 12 : i % 12, room1);
            seatRepository.save(seat);
        }
        for (int i = 1; i <= 150; i++) {
            Seat seat = new Seat(i / 15 + 1, i % 15 == 0 ? 15 : i % 15, room2);
            seatRepository.save(seat);
        }

        logger.info("Theatres, rooms, and seats have been initialized.");
    }

    private void initializeShowtimes() {
        List<Movie> movies = movieRepository.findAll();
        List<TheatreRoom> rooms = theatreRoomRepository.findAll();

        Showtime showtime1 = new Showtime(LocalDateTime.of(2024, 11, 26, 11, 30), false, rooms.get(0), movies.get(0));
        Showtime showtime2 = new Showtime(LocalDateTime.of(2024, 12, 1, 14, 0), true, rooms.get(1), movies.get(1));

        showtimeRepository.saveAll(List.of(showtime1, showtime2));
        logger.info("Showtimes have been initialized.");
    }

    private void initializeSeatAvailabilities() {
        List<Showtime> showtimes = showtimeRepository.findAll();

        for (Showtime showtime : showtimes) {
            List<Seat> seats = seatRepository.findByTheatreRoom(showtime.getTheatreRoom());
            for (Seat seat : seats) {
                SeatAvailability seatAvailability = new SeatAvailability();
                seatAvailability.setAvailable(true);
                seatAvailability.setShowtime(showtime);
                seatAvailability.setSeat(seat);
                seatAvailabilityRepository.save(seatAvailability);
            }
        }
        logger.info("Seat availabilities have been initialized.");
    }

    private void initializeTicketsPaymentsAndReceipts() {
        List<Seat> seats = seatRepository.findByTheatreRoom(theatreRoomRepository.findAll().get(0));
        Showtime showtime = showtimeRepository.findAll().get(0);
        AcmePlexUser user = userRepository.findAll().get(0);

        for (int i = 0; i < 5; i++) {
            Ticket ticket = new Ticket("confirmed", new java.util.Date(), showtime, seats.get(i), user);
            ticketRepository.save(ticket);

            Payment payment = new Payment(15.0f, new java.util.Date(), "completed", "credit card", ticket, user);
            paymentRepository.save(payment);

            Receipt receipt = new Receipt(payment, user);
            receiptRepository.save(receipt);

            AcmePlexCredit credit = new AcmePlexCredit("CRED12345-" + i, 5.0f, payment, new java.util.Date());
            creditRepository.save(credit);
        }
        logger.info("Tickets, payments, receipts, and credits have been initialized.");
    }
}
