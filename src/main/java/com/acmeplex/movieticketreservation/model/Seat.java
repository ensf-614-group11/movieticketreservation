package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`row_number`", columnDefinition = "INT NOT NULL")
    private int rowNumber;

    @Column(name = "seat_number", columnDefinition = "INT NOT NULL")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "theatre_room_id")
    @JsonIgnore
    private TheatreRoom theatreRoom;

    @OneToOne(mappedBy = "seat")
    private Ticket ticket;


    public Seat(int rowNumber, int seatNumber, TheatreRoom theatreRoom) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.theatreRoom = theatreRoom;
    }

    public Seat() {

    }

    public Long getId() {
        return id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public TheatreRoom getTheatreRoom() {
        return theatreRoom;
    }

    public void setTheatreRoom(TheatreRoom theatreRoom) {
        this.theatreRoom = theatreRoom;
    }
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}
