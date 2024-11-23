package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a user in the AcmePlex movie ticket reservation system.
 * Each user has a unique ID, email, password, first name, and last name.
 */
@Entity
public class AcmePlexUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generated unique ID
    private Long id;

    @Column(nullable = false, unique = true) // Ensure email is unique and non-null
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean activeMembership;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "acmePlexUser", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "acmePlexUser", cascade = CascadeType.ALL)
    private List<Receipt> receipts;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "billing_address_id")
    private Address savedBillingAddress;

    @OneToMany(mappedBy = "acmePlexUser", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = false)
    private List<PaymentType> paymentTypes;

    /**
     * Default constructor required by JPA.
     */
    public AcmePlexUser() {
    }

    /**
     * Constructs a new AcmePlexUser with the specified email, password, first name,
     * and last name.
     *
     * @param email     the email of the user (must be unique)
     * @param password  the encrypted password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     */
    public AcmePlexUser(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return the user's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the user's email address.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email to set (must be unique)
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's encrypted password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the encrypted password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's first name.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public List<PaymentType> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(List<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public Address getSavedBillingAddress() {
        return savedBillingAddress;
    }

    public void setSavedBillingAddress(Address savedBillingAddress) {
        this.savedBillingAddress = savedBillingAddress;
    }

    public boolean isActiveMembership() {
        return activeMembership;
    }

    public void activateMembership() {
        this.activeMembership = true;
    }

    public void deactivateMembership() {
        this.activeMembership = false;
    }
}
