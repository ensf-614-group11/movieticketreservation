package com.acmeplex.movieticketreservation.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for generating, validating, and extracting information from JWT tokens.
 * Provides methods for token creation, validation, and extraction of the user’s email.
 */
@Component
public class AuthTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenUtil.class); // Logger instance
    private final String SECRET_KEY = "your_secret_key"; // Use a strong key in production

    /**
     * Generates a JWT token with the specified email as the subject.
     *
     * @param email the email of the user, used as the token subject
     * @return a signed JWT token as a String
     */
    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            // Create and sign the JWT
            String token = JWT.create()
                    .withSubject(email) // Use email as the subject
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                    .sign(algorithm);

            logger.info("Token generated successfully for email: {}", email);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token for email: {}", email, e);
            throw new RuntimeException("Error generating token for email: " + email, e);
        }
    }

    /**
     * Validates the JWT token against the specified email.
     *
     * @param token the JWT token
     * @param email the email to validate against
     * @return true if the token is valid and belongs to the specified email, false otherwise
     */
    public boolean validateToken(String token, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            boolean isValid = jwt.getSubject().equals(email) && !isTokenExpired(jwt);

            if (isValid) {
                logger.info("Token successfully validated for email: {}", email);
            } else {
                logger.warn("Invalid token for email: {}", email);
            }
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating token for email: {}", email, e);
            return false; // Token is invalid or expired
        }
    }

    /**
     * Checks if the token has expired.
     *
     * @param jwt the decoded JWT
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(DecodedJWT jwt) {
        boolean expired = jwt.getExpiresAt().before(new Date());
        if (expired) {
            logger.warn("Token has expired: {}", jwt.getSubject());
        }
        return expired;
    }

    /**
     * Extracts the email (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the email as a String
     */
    public String extractUsername(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            String email = jwt.getSubject();
            logger.info("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("Error extracting email from token", e);
            throw new RuntimeException("Error extracting email from token", e);
        }
    }
}
