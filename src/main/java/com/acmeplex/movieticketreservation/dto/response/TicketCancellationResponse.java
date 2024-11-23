package com.acmeplex.movieticketreservation.dto.response;

import com.acmeplex.movieticketreservation.model.Ticket;

public class TicketCancellationResponse {

    private Ticket ticket;
    private String message;

    public TicketCancellationResponse(Ticket ticket, String message) {
        this.ticket = ticket;
        this.message = message;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
