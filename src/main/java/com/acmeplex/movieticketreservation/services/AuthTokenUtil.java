package com.acmeplex.movieticketreservation.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for generating, validating, and extracting information from JWT tokens.
 * This class includes logging for debugging and error handling.
 */
@Component
public class AuthTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenUtil.class); // Logger instance
    private final String SECRET_KEY = "your_secret_key"; // Use a strong key in production

    /**
     * Generates a JWT token with the specified email.
     *
     * @param email the email of the user
     * @return a signed JWT token as a String
     */
    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            // Create and sign the JWT
            return JWT.create()
                    .withSubject(email) // Use email as the subject
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                    .sign(algorithm);
        } catch (Exception e) {
            // Log any errors during token generation
            logger.error("Error occurred while generating the token for email: {}", email, e);
            throw new RuntimeException("Error occurred while generating the token", e);
        }
    }

    /**
     * Validates the JWT token against the specified username.
     *
     * @param token    the JWT token
     * @param username the username to validate against
     * @return true if the token is valid and belongs to the specified username, false otherwise
     */
    public boolean validateToken(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            // Validate if the token belongs to the specified user
            return jwt.getSubject().equals(username) && !isTokenExpired(jwt);
        } catch (Exception e) {
            // Log any errors during token validation
            logger.error("Error occurred while validating the token for user: {}", username, e);
            return false; // Token is invalid
        }
    }

    /**
     * Checks if the token has expired.
     *
     * @param jwt the decoded JWT
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(DecodedJWT jwt) {
        try {
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            // Log errors in checking expiration
            logger.error("Error occurred while checking expiration of token", e);
            return true; // Default to expired if there's an issue
        }
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username as a String
     */
    public String extractUsername(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            // Log errors in extracting username
            logger.error("Error occurred while extracting username from token", e);
            throw new RuntimeException("Error occurred while extracting username from token", e);
        }
    }
}
