package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

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

    /**
     * Default constructor required by JPA.
     */
    public AcmePlexUser() {}

    /**
     * Constructs a new AcmePlexUser with the specified email, password, first name, and last name.
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
}
