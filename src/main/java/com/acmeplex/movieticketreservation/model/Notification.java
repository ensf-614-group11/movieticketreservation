package com.acmeplex.movieticketreservation.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;
    private Date dateSent;
    private String emailRecipient;
    private String message;

    public Notification(Date dateSent, String emailRecipient, String message) {
        this.dateSent = dateSent;
        this.emailRecipient = emailRecipient;
        this.message = message;
    }

    public Notification() {

    }


    public Long getId() {
        return id;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
