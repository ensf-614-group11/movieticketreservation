package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float amount;
    private Date datePaid;
    private String status;
    private String method;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = true)
    private Ticket ticket;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Receipt receipt;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private AcmePlexCredit credit;

    @ManyToOne
    @JsonIgnore
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

    public Payment(float amount, Date datePaid, String status, String method, Ticket ticket) {
        this.amount = amount;
        this.datePaid = datePaid;
        this.status = status;
        this.method = method;
        this.ticket = ticket;
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

    public AcmePlexCredit getAcmePlexCredit() {
        return credit;
    }

    public void setCredit(AcmePlexCredit credit) {
        this.credit = credit;
    }

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }
}
