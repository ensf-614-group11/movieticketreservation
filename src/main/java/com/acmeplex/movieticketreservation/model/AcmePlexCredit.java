package com.acmeplex.movieticketreservation.model;


import jakarta.persistence.*;

@Entity
public class AcmePlexCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creditSecretCode;
    private float amount;

    @OneToOne
    @JoinColumn(name = "payment_id")  // Foreign key to the payment table
    private Payment payment;

    public AcmePlexCredit(String creditSecretCode, float amount, Payment payment) {
        this.creditSecretCode = creditSecretCode;
        this.amount = amount;
        this.payment = payment;
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
}
