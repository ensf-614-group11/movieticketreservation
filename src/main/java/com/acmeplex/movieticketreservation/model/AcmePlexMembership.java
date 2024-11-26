package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class AcmePlexMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean activeMembership;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date renewalDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(nullable = false)
    private boolean renewalStatus;

    public AcmePlexMembership() {
        this.activeMembership = true;
        this.renewalStatus = true;
        this.renewalDate = new Date();
        this.expiryDate = calculateDefaultExpirationDate(new Date());
    }

    public AcmePlexMembership(Date dateRegistered, Date expiryDate) {
        this.activeMembership = true;
        this.renewalStatus = true;
        this.renewalDate = dateRegistered;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public boolean isActiveMembership() {
        return activeMembership;
    }

    public void setActiveMembership(boolean activeMembership) {
        this.activeMembership = activeMembership;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Date getExpiryDate() { return expiryDate; }

    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(boolean renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    private Date calculateDefaultExpirationDate(Date startDate) {
        return new Date(startDate.getTime() + 365L * 24 * 60 * 60 * 1000); // Add 1 year in milliseconds
    }
}
