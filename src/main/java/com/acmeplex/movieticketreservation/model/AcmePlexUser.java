package com.acmeplex.movieticketreservation.model;

import com.acmeplex.movieticketreservation.observer.Observer;
import com.acmeplex.movieticketreservation.services.EmailService;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * Represents a user in the AcmePlex movie ticket reservation system.
 * Each user has a unique ID, email, password, first name, and last name.
 */
@Entity
public class AcmePlexUser implements Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generated unique ID
    private Long id;

    @Column(nullable = false, unique = true) // Ensure email is unique and non-null
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateRegistered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode countryCode;

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

    @OneToMany(mappedBy = "acmePlexUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<PaymentType> paymentTypes;

    @OneToMany(mappedBy = "acmePlexUser", cascade = CascadeType.ALL)
    private List<AcmePlexCredit> credits;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "membership_id", referencedColumnName = "id")
    private AcmePlexMembership membership;

    public AcmePlexUser() {}

    /**
     * Constructs a new AcmePlexUser with the specified email, password, first name, and last name.
     *
     * @param email     the email of the user (must be unique)
     * @param password  the encrypted password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     */
    public AcmePlexUser(String email,
                        String password,
                        String firstName,
                        String lastName,
                        CountryCode countryCode) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryCode = countryCode;
        dateRegistered = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CountryCode getCountryCode() { return countryCode; }

    public void setCountryCode(CountryCode countryCode) { this.countryCode = countryCode; }

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

    public AcmePlexMembership getMembership() { return membership; }

    public void setMembership(AcmePlexMembership membership) { this.membership = membership; }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }

    public List<AcmePlexCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<AcmePlexCredit> credits) {
        this.credits = credits;
    }

    public void addCredit(AcmePlexCredit credit) {
        credits.add(credit);
        credit.setAcmePlexUser(this); // Set the user on the credit
    }

    public void removeCredit(AcmePlexCredit credit) {
        credits.remove(credit);
        credit.setAcmePlexUser(null); // Optionally nullify the user reference
    }

    // Method to remove the oldest credit from the list, to be used for payment processing
    public AcmePlexCredit removeOldestCredit() {
        if (credits != null && !credits.isEmpty()) {
            AcmePlexCredit oldestCredit = credits.getFirst();  // Get the first credit (oldest)
            credits.removeFirst();  // Remove the first credit from the list
            oldestCredit.setAcmePlexUser(null);  // Nullify the user reference
            return oldestCredit;  // Return the removed credit
        }
        return null;  // Return null if the list is empty
    }
    public double getTotalCredits() {
        return credits.stream().mapToDouble(AcmePlexCredit::getAmount).sum();
    }


    @Override
    public void update(String message) {
        // When a user receives a new message with movie news, send an email and save it to their notifications
        EmailService emailService = new EmailService();
        emailService.sendSimpleMessage(this.email, "AcmePlex Movie News", message);
        Notification notification = new Notification(new Date(), message, this.email);
        addNotification(notification);
    }


    public Date getDateRegistered() {return dateRegistered;}
}
