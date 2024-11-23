package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.dto.response.TicketCancellationResponse;
import com.acmeplex.movieticketreservation.model.Ticket;
import com.acmeplex.movieticketreservation.services.TicketCancellationService;
import com.acmeplex.movieticketreservation.services.exceptions.TicketNotFoundException;
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
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> getTicketDetails(@PathVariable Long ticketId) {
        // Retrieve ticket details by ID
        Optional<Ticket> ticket = ticketCancellationService.getTicketById(ticketId);
        if (ticket.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found for ID: " + ticketId);
        }

        // Prepare the cancellation response
        TicketCancellationResponse cancellationResponse = ticketCancellationService.prepareTicketDetailsForCancellation(ticket.get());

        // Wrap the response in an ApiResponse and return it in a ResponseEntity
        ApiResponse<TicketCancellationResponse> apiResponse = new ApiResponse<>(
                true,
                "Ticket details retrieved successfully.",
                cancellationResponse
        );
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint to cancel a ticket for a registered user
    @PostMapping(Constants.Endpoints.API_TICKET_CANCELLATION_POST_REGISTERED)
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> cancelTicket(@PathVariable Long ticketId) {
        // Retrieve ticket details and process cancellation
        Optional<Ticket> ticket = ticketCancellationService.getTicketById(ticketId);
        if (ticket.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found for ID: " + ticketId);
        }

        // Process the cancellation
        TicketCancellationResponse cancellationResponse = ticketCancellationService.cancelTicketRegisteredUser(ticket.get());

        // Wrap the cancellation response in an ApiResponse and return it in a ResponseEntity
        ApiResponse<TicketCancellationResponse> apiResponse = new ApiResponse<>(
                true,
                "Ticket successfully cancelled.",
                cancellationResponse
        );
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint to cancel a ticket for ordinary users
    @PostMapping(Constants.Endpoints.API_TICKET_CANCELLATION_POST_ORDINARY)
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> cancelTicketForOrdinaryUser(@PathVariable Long ticketId, @RequestParam String email) {
        Optional<Ticket> ticket = ticketCancellationService.getTicketById(ticketId);
        if (ticket.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found for ID: " + ticketId);
        }

        // Cancel the ticket
        TicketCancellationResponse cancellationResponse = ticketCancellationService.cancelTicketOrdinaryUser(ticket.get(), email);
        ApiResponse<TicketCancellationResponse> apiResponse = new ApiResponse<>(true, "Ticket cancelled successfully.", cancellationResponse);
        return ResponseEntity.ok(apiResponse);
    }







}
