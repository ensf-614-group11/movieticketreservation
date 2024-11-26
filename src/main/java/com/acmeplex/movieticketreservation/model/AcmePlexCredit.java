package com.acmeplex.movieticketreservation.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AcmePlexCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creditSecretCode;
    private float amount;
    private Date dateCreated;
    private Date expirationDate;

    @OneToOne
    @JoinColumn(name = "payment_id")  // Foreign key to the payment table
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser acmePlexUser;

    public AcmePlexCredit(String creditSecretCode, float amount, Payment payment, Date dateCreated) {
        this.creditSecretCode = creditSecretCode;
        this.amount = amount;
        this.payment = payment;
        this.dateCreated = dateCreated;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateCreated);
        calendar.add(Calendar.YEAR, 1);
        expirationDate = calendar.getTime();
    }

    public AcmePlexCredit() {

    }

    public Long getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getCreditSecretCode() {return creditSecretCode;}

    public void setCreditSecretCode(String creditSecretCode) {this.creditSecretCode = creditSecretCode;}

    public float getAmount() {return amount;}

    public void setAmount(float amount) {this.amount = amount;}

    public Date getDateCreated() {return dateCreated;}

    public void setDateCreated(Date dateCreated) {this.dateCreated = dateCreated;}

    public Date getExpirationDate() {return expirationDate;}

    public void setExpirationDate(Date expirationDate) {this.expirationDate = expirationDate;}

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }
}
