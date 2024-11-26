package com.acmeplex.movieticketreservation.dto.request;

public class TicketCancellationOrdinaryRequest {
    private Long ticketId;
    private String email;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
