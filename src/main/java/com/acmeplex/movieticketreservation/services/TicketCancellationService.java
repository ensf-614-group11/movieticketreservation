package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.TicketCancellationResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.model.SeatAvailability;
import com.acmeplex.movieticketreservation.model.Showtime;
import com.acmeplex.movieticketreservation.model.Ticket;
import com.acmeplex.movieticketreservation.repository.SeatAvailabilityRepository;
import com.acmeplex.movieticketreservation.repository.TicketRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TicketCancellationService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    private NotificationService notificationService = new NotificationService();

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(Math.toIntExact(ticketId));
    }

    // REGISTERED USER AND ORDINARY USER
    // Prepare ticket details for cancellation (for the GET request)
    public TicketCancellationResponse prepareTicketDetailsForCancellation (Ticket ticket) {
        if (isWithinCancellableWindow(ticket.getShowtime().getDateAndTime())) {
            return new TicketCancellationResponse(ticket, "Ticket can be cancelled.");
        } else {
            throw new InvalidRequestException("Ticket cannot be cancelled as the showtime is within 72 hours.");
        }
    }

    // REGISTERED USER
    // Cancel the ticket (for the POST request)
    public TicketCancellationResponse cancelTicketRegisteredUser(Ticket ticket) {
        if (!isWithinCancellableWindow(ticket.getShowtime().getDateAndTime())) {
            throw new InvalidRequestException("Ticket cannot be cancelled as the showtime is within 72 hours.");
        }

        // Mark the ticket as cancelled
        ticket.setStatus("Cancelled");
        ticketRepository.save(ticket);

        // Update the seat availability
        Optional<SeatAvailability> seatAvailabilityOpt = seatAvailabilityRepository.findBySeatId(ticket.getSeat().getId());
        if (seatAvailabilityOpt.isPresent()) {
            SeatAvailability seatAvailability = seatAvailabilityOpt.get();
            seatAvailability.setAvailable(true);
            seatAvailabilityRepository.save(seatAvailability);
        }

        // Create a new credit
        Date currentDate = new Date();
        AcmePlexCredit acmePlexCredit = new AcmePlexCredit(creditSecretCodeGenerator(), ticket.getPayment().getAmount(), ticket.getPayment(), currentDate);
        acmePlexCredit.setAcmePlexUser(ticket.getUser());
        ticket.getUser().addCredit(acmePlexCredit);

        // Send notification to confirm cancellation
        notificationService.sendTicketCancellationRegisteredUser(acmePlexCredit, ticket.getUser());


       return new TicketCancellationResponse(ticket, "Ticket successfully cancelled.");

    }

    // ORDINARY USER
    // Cancel the ticket (for the POST request)
    public TicketCancellationResponse cancelTicketOrdinaryUser(Ticket ticket, String email) {
        if (!isWithinCancellableWindow(ticket.getShowtime().getDateAndTime())) {
            throw new InvalidRequestException("Ticket cannot be cancelled as the showtime is within 72 hours.");
        }

        // Mark the ticket as cancelled
        ticket.setStatus("Cancelled");
        ticketRepository.save(ticket);

        // Update the seat availability
        Optional<SeatAvailability> seatAvailabilityOpt = seatAvailabilityRepository.findBySeatId(ticket.getSeat().getId());
        if (seatAvailabilityOpt.isPresent()) {
            SeatAvailability seatAvailability = seatAvailabilityOpt.get();
            seatAvailability.setAvailable(true);
            seatAvailabilityRepository.save(seatAvailability);
        }

        // Create a new credit
        Date currentDate = new Date();
        AcmePlexCredit acmePlexCredit = new AcmePlexCredit(creditSecretCodeGenerator(), (float) ((ticket.getPayment().getAmount())*0.85), ticket.getPayment(), currentDate);

        // Send notification to confirm cancellation
        notificationService.sendTicketCancellationOrdinaryUser(acmePlexCredit, email);

        return new TicketCancellationResponse(ticket, "Ticket successfully cancelled.");

    }



    // Helper method
    // Check if the showtime is more than 72 hours away from the current time
    private boolean isWithinCancellableWindow(LocalDateTime showtimeDate) {
       LocalDateTime now = LocalDateTime.now();

       LocalDateTime deadline = now.plusHours(72);
        return showtimeDate.isAfter(deadline);
    }


    // Helper method
    // Generate a secret code for the credit
    private String creditSecretCodeGenerator () {
       final String CHARACTERS =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(10);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }


}
