package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float amount;
    private String status;
    private String method;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser acmePlexUser;

    public PaymentType() {}

    public PaymentType(float amount, String status, String method, AcmePlexUser acmePlexUser) {
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.acmePlexUser = acmePlexUser;
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

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }
}
