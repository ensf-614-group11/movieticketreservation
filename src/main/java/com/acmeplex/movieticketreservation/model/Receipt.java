package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "payment_id")  // Foreign key to Payment
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser acmePlexUser;

    public Receipt(Payment payment, AcmePlexUser acmePlexUser) {
        this.payment = payment;
        this.acmePlexUser = acmePlexUser;

    }

    public Receipt(Payment payment) {
        this.payment = payment;
    }

    public Receipt() {

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

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }

}
