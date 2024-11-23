package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.dto.response.TicketCancellationResponse;
import com.acmeplex.movieticketreservation.model.Ticket;
import com.acmeplex.movieticketreservation.services.NotificationService;
import com.acmeplex.movieticketreservation.services.TicketCancellationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Constants.Endpoints.API_TICKET_CANCELLATION)
public class TicketCancellationController {

    @Autowired
    private TicketCancellationService ticketCancellationService;

    // Endpoint to retrieve ticket details based on ticket ID
    @GetMapping(Constants.Endpoints.API_TICKET_CANCELLATION_GET)
    public TicketCancellationResponse getTicketDetails(@PathVariable Long ticketId) {
        // Retrieve ticket details by ID
        Optional<Ticket> ticket = ticketCancellationService.getTicketById(ticketId);
        if (ticket.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found for ID: " + ticketId);
        }
        return ticketCancellationService.prepareTicketDetailsForCancellation(ticket.get());
    }

    // Endpoint to cancel a ticket
    @PostMapping(Constants.Endpoints.API_TICKET_CANCELLATION_POST)
    @ResponseStatus(HttpStatus.OK)
    public TicketCancellationResponse cancelTicket(@PathVariable Long ticketId) {
        // Retrieve ticket details and process cancellation
        Optional<Ticket> ticket = ticketCancellationService.getTicketById(ticketId);
        if (ticket.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found for ID: " + ticketId);
        }

        return ticketCancellationService.cancelTicket(ticket.get());
    }







    @GetMapping(Constants.Endpoints.API_TICKET_CANCELLATION_GET)
    public ResponseEntity<ApiResponse<Ticket>> getTicket(Long ticketId) {


    }
}
