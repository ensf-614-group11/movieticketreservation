package com.acmeplex.movieticketreservation.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateSent;
    private String emailRecipient;

    @Column(length = 10000)
    private String message;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser user;

    // Constructor for registered user
    public Notification(Date dateSent, String message, AcmePlexUser user) {
        this.dateSent = dateSent;
        this.user = user;
        this.message = message;
        this.emailRecipient = null;
    }

    // Constructor for ordinary user
    public Notification(Date dateSent, String message, String emailRecipient) {
        this.dateSent = dateSent;
        this.emailRecipient = emailRecipient;
        this.message = message;
        this.user = null;
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

    public AcmePlexUser getUser() {
        return user;
    }

    public void setUser(AcmePlexUser user) {
        this.user = user;
    }
}
