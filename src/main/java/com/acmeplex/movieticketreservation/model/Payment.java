package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long id;
    private float amount;
    private Date datePaid;
    private String status;
    private String method;


    public Payment(float amount, Date datePaid, String status, String method) {
        this.amount = amount;
        this.datePaid = datePaid;
        this.status = status;
        this.method = method;
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
}
