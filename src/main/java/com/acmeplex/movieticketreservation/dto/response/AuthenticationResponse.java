package com.acmeplex.movieticketreservation.dto.response;

/**
 * Represents a response for successful authentication, containing the JWT token and the authenticated user's email.
 * This class is used to return the authentication token and user information after a successful login.
 */
public class AuthenticationResponse {

    private String token;
    private String email;

    /**
     * Constructs an {@code AuthenticationResponse} with the provided token and email.
     *
     * @param token the JWT token generated after authentication
     * @param email the email of the authenticated user
     */
    public AuthenticationResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    /**
     * Returns the JWT token for the authenticated session.
     *
     * @return the authentication token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token for the authenticated session.
     *
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the email of the authenticated user.
     *
     * @return the authenticated user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the authenticated user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
