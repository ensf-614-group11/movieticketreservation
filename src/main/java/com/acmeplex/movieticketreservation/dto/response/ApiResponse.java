package com.acmeplex.movieticketreservation.dto.response;

/**
 * A generic class for API responses, encapsulating a success status, message, and optional data.
 *
 * @param <T> the type of the data included in the response, if any
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    /**
     * Constructs an {@code ApiResponse} with a success status, message, and optional data.
     *
     * @param success whether the response indicates success
     * @param message a message providing additional information about the response
     * @param data    the data included in the response, if applicable
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructs an {@code ApiResponse} with a success status and message, without data.
     *
     * @param success whether the response indicates success
     * @param message a message providing additional information about the response
     */
    public ApiResponse(boolean success, String message) {
        this(success, message, null);
    }

    /**
     * Returns whether the response indicates success.
     *
     * @return {@code true} if the response indicates success, {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the response.
     *
     * @param success {@code true} if the response should indicate success, {@code false} otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the message providing additional information about the response.
     *
     * @return the message associated with the response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message providing additional information about the response.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the data included in the response, if any.
     *
     * @return the data associated with the response, or {@code null} if no data is present
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data to be included in the response.
     *
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}
