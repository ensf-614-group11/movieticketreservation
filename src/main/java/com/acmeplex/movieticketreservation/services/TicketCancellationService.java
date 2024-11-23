package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.TicketCancellationResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.model.Ticket;
import com.acmeplex.movieticketreservation.repository.TicketRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.InvalidCancellationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TicketCancellationService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private NotificationService notificationService = new NotificationService();

    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(Math.toIntExact(ticketId));
    }

    // REGISTERED USER
    // Prepare ticket details for cancellation (for the GET request)
    public TicketCancellationResponse prepareTicketDetailsForCancellation (Ticket ticket) {
        if (isWithinCancellableWindow(ticket.getShowtime())) {
            return new TicketCancellationResponse(ticket, "Ticket can be cancelled.");
        } else {
            throw new InvalidCancellationException("Ticket cannot be cancelled as the showtime is within 72 hours.");
        }
    }

    // Cancel the ticket (for the POST request)
    public TicketCancellationResponse cancelTicket(Ticket ticket) {
        if (!isWithinCancellableWindow(ticket.getShowtime())) {
            throw new InvalidCancellationException("Ticket cannot be cancelled as the showtime is within 72 hours.");
        }

        // Mark the ticket as cancelled
        ticket.setStatus("Cancelled");
        ticketRepository.save(ticket);

        // Update the seat availability

        // Create a new credit
        Date currentDate = new Date();
        AcmePlexCredit acmePlexCredit = new AcmePlexCredit("secretCode", ticket.getPayment().getAmount(), ticket.getPayment(), currentDate);
        acmePlexCredit.setAcmePlexUser(ticket.getUser());

        // Send notification to confirm cancellation
        notificationService.sendTicketCancellationRegisteredUser(acmePlexCredit, ticket.getUser());


       return new TicketCancellationResponse(ticket, "Ticket successfully cancelled.");

    }

    // ORDINARY USER



    // Check if the showtime is more than 72 hours away from the current time
    private boolean isWithinCancellableWindow(Date showtime) {
        return showtime.isAfter(LocalDateTime.now().plusHours(72));
    }


}
