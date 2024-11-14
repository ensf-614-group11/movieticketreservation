package com.acmeplex.movieticketreservation.dto.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a login request containing user email and password for authentication.
 */
public class LoginRequest {

    private static final Logger logger = LoggerFactory.getLogger(LoginRequest.class);

    private String email;
    private String password;

    /**
     * Default constructor for LoginRequest.
     */
    public LoginRequest() {
    }

    /**
     * Constructor to initialize LoginRequest with email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        logger.info("LoginRequest object created with email: {}", email);
    }

    /**
     * Returns the email associated with the login request.
     *
     * @return the user's email
     */
    public String getEmail() {
        try {
            logger.debug("Getting email: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("Error retrieving email", e);
            throw e; // Rethrow the exception if necessary
        }
    }

    /**
     * Sets the email for the login request.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        try {
            logger.debug("Setting email: {}", email);
            this.email = email;
        } catch (Exception e) {
            logger.error("Error setting email", e);
            throw e; // Rethrow the exception if necessary
        }
    }

    /**
     * Returns the password associated with the login request.
     *
     * @return the user's password
     */
    public String getPassword() {
        try {
            logger.debug("Getting password");
            return password;
        } catch (Exception e) {
            logger.error("Error retrieving password", e);
            throw e; // Rethrow the exception if necessary
        }
    }

    /**
     * Sets the password for the login request.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        try {
            logger.debug("Setting password");
            this.password = password;
        } catch (Exception e) {
            logger.error("Error setting password", e);
            throw e; // Rethrow the exception if necessary
        }
    }
}
