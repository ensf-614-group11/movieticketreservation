package com.acmeplex.movieticketreservation.dto.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * Represents an error response structure for API responses, providing details about the error, status, and request path.
 * This class captures the error details and logs any errors for traceability.
 */
public class ErrorResponse {

    private static final Logger logger = LoggerFactory.getLogger(ErrorResponse.class); // Logger for this class

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    /**
     * Constructs an {@code ErrorResponse} with the specified details.
     * This constructor logs the error details for traceability.
     *
     * @param timestamp the time when the error occurred
     * @param status    the HTTP status code associated with the error
     * @param error     a short description of the error
     * @param message   a detailed message about the error
     * @param path      the URI path of the request that caused the error
     */
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;

        // Log the error details for tracking
        logger.error("Error occurred at {}. Status: {}, Error: {}, Message: {}, Path: {}",
                timestamp, status, error, message, path);
    }

    /**
     * Returns the timestamp when the error occurred.
     *
     * @return the timestamp of the error
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the error.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the HTTP status code associated with the error.
     *
     * @return the status code of the error
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code for the error.
     *
     * @param status the status code to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns a short description of the error.
     *
     * @return the error description
     */
    public String getError() {
        return error;
    }

    /**
     * Sets a short description of the error.
     *
     * @param error the error description to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Returns a detailed message about the error.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets a detailed message about the error.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the URI path of the request that caused the error.
     *
     * @return the request path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the URI path of the request that caused the error.
     *
     * @param path the request path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
