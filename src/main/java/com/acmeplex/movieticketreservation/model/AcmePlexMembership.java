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
    private boolean renewalStatus;

    public AcmePlexMembership() {
        this.activeMembership = true;
        this.renewalStatus = true;
        this.renewalDate = new Date();
    }

    public AcmePlexMembership(Date dateRegistered) {
        this.activeMembership = true;
        this.renewalStatus = true;
        this.renewalDate = dateRegistered;
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

    public boolean isRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(boolean renewalStatus) {
        this.renewalStatus = renewalStatus;
    }
}
