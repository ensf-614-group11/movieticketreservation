package com.acmeplex.movieticketreservation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a login request containing user email and password for authentication.
 */
public class DefaultLoginRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    /**
     * Default constructor for LoginRequest.
     */
    public DefaultLoginRequest() {
    }

    /**
     * Constructor to initialize LoginRequest with email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     */
    public DefaultLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
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
}