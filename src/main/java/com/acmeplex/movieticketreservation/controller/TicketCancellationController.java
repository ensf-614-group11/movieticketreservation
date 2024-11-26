package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.TicketCancellationOrdinaryRequest;
import com.acmeplex.movieticketreservation.dto.request.TicketCancellationRegisteredRequest;
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
@RequestMapping("/api/cancel-ticket")
public class TicketCancellationController {

    @Autowired
    private TicketCancellationService ticketCancellationService;

    // Endpoint to retrieve ticket details based on ticket ID
    @GetMapping("/get")
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> getTicketDetails(@RequestParam Long ticketId) {
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
    @PostMapping("/post/registered")
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> cancelTicket(@RequestBody TicketCancellationRegisteredRequest request) {
        Long ticketId = request.getTicketId();
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
    @PostMapping("/post/ordinary")
    public ResponseEntity<ApiResponse<TicketCancellationResponse>> cancelTicketForOrdinaryUser(@RequestBody TicketCancellationOrdinaryRequest request) {
       Long ticketId = request.getTicketId();
       String email = request.getEmail();

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
