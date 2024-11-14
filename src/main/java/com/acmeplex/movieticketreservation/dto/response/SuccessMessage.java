package com.acmeplex.movieticketreservation.dto.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a simple success message response for API operations.
 * This DTO is used to provide a success message back to the client.
 */
public class SuccessMessage {

    private static final Logger logger = LoggerFactory.getLogger(SuccessMessage.class);  // Logger for this class

    private String message;

    /**
     * Constructs a {@code SuccessMessage} with a success message.
     * Logs the creation of a success message for traceability.
     *
     * @param message the success message to include in the response
     */
    public SuccessMessage(String message) {
        this.message = message;
        logger.info("SuccessMessage created with message: {}", message);  // Log the success message creation
    }

    /**
     * Returns the success message for the response.
     *
     * @return the success message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the success message for the response.
     * Logs when the message is set or updated.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
        logger.debug("SuccessMessage set to: {}", message);  // Log when the message is set
    }

    /**
     * Logs and throws an exception if the success message is invalid (null or empty).
     * This method is a sample of how to handle validation before setting the message.
     */
    public void validateMessage() {
        try {
            if (this.message == null || this.message.isEmpty()) {
                logger.error("Invalid success message: {}", this.message);
                throw new IllegalArgumentException("Message cannot be null or empty");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for success message: {}", e.getMessage());
            throw e;  // Rethrow the exception after logging
        }
    }
}
