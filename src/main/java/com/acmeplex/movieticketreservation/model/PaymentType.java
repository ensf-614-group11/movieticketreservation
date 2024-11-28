package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // E.g., "Active" or "Inactive"
    private String type; // E.g., "CREDIT_CARD", "PAYPAL"

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = true)
    private AcmePlexUser acmePlexUser;

    public PaymentType() {}

    public PaymentType(String status, String type, AcmePlexUser acmePlexUser) {
        this.status = status;
        this.type = type;
        this.acmePlexUser = acmePlexUser;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setMethod(String method) {
        this.type = method;
    }

    public AcmePlexUser getAcmePlexUser() {
        return acmePlexUser;
    }

    public void setAcmePlexUser(AcmePlexUser acmePlexUser) {
        this.acmePlexUser = acmePlexUser;
    }
}