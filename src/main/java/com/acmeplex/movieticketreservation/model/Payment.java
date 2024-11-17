package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float amount;
    private Date datePaid;
    private String status;
    private String method;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Receipt receipt;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Credit credit;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser acmePlexUser;

    public Payment(float amount, Date datePaid, String status, String method, Ticket ticket, AcmePlexUser acmePlexUser) {
        this.amount = amount;
        this.datePaid = datePaid;
        this.status = status;
        this.method = method;
        this.ticket = ticket;
        this.acmePlexUser = acmePlexUser;
    }

    public Payment() {

    }


    public Long getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }
}
