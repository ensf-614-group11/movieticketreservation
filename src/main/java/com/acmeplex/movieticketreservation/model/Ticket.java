package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateIssued;
    private String status; // confirmed or cancelled

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @OneToOne(mappedBy = "ticket")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser user;


    // Constructor for registered users
    public Ticket(String status, Date dateIssued, Showtime showtime, Seat seat, AcmePlexUser user) {
        this.status = status;
        this.dateIssued = dateIssued;
        this.showtime = showtime;
        this.seat = seat;
        this.user = user;
    }

    // Constructor for ordinary users
    public Ticket(String status, Date dateIssued, Showtime showtime, Seat seat) {
        this.status = status;
        this.dateIssued = dateIssued;
        this.showtime = showtime;
        this.seat = seat;
    }

    public Ticket() {

    }

    public Long getId() {
        return id;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public AcmePlexUser getUser() {return user;}

    public void setUser(AcmePlexUser user) {this.user = user;}


}
