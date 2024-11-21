package com.acmeplex.movieticketreservation.auth.strategy;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.LoginRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Strategy for handling user login.
 */
@Component
public class DefaultLoginStrategy implements AuthStrategy<LoginRequest> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLoginStrategy.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil;

    public DefaultLoginStrategy(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
    }

    @Override
    public ResponseEntity<ApiResponse<String>> execute(LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for email: {}", loginRequest.getEmail());

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Generate JWT token
            String token = authTokenUtil.generateToken(loginRequest.getEmail());
            logger.info("User {} authenticated successfully.", loginRequest.getEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.USER_LOGGED_IN, token));
        } catch (AuthenticationException ex) {
            logger.warn("Authentication failed for user: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, Constants.Errors.BAD_REQUEST));
        } catch (Exception ex) {
            logger.error("Unexpected error during login for user: {}", loginRequest.getEmail(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, Constants.Errors.INTERNAL_SERVER_ERROR));
        }
    }
}
