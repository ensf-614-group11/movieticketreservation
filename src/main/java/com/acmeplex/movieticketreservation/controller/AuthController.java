package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.LoginRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
import com.acmeplex.movieticketreservation.services.AcmePlexUserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user authentication and registration.
 */
@RestController
@RequestMapping(Constants.Endpoints.API_AUTH)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil;
    private final AcmePlexUserProfileService userProfileService;

    /**
     * Constructs the AuthController with required dependencies.
     *
     * @param authenticationManager the authentication manager for user authentication
     * @param authTokenUtil         utility for JWT token generation and validation
     * @param userProfileService    the service handling user profile and registration logic
     */
    public AuthController(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil, AcmePlexUserProfileService userProfileService) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
        this.userProfileService = userProfileService;
    }

    /**
     * Authenticates a user and generates a JWT token if successful.
     *
     * @param loginRequest the login request containing username and password
     * @return a ResponseEntity containing an ApiResponse with the JWT token on successful authentication
     */
    @PostMapping(Constants.Endpoints.API_AUTH_LOGIN)
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest loginRequest) {
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

    /**
     * Registers a new user if the email is not already in use.
     *
     * @param registerRequest the registration request containing user details
     * @return a ResponseEntity containing an ApiResponse with a success message on successful registration
     */
    @PostMapping(Constants.Endpoints.API_AUTH_REGISTER)
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            logger.info("Registration attempt for email: {}", registerRequest.getEmail());

            // Register the user using the user profile service
            AcmePlexUser newUser = userProfileService.registerUser(registerRequest);

            // Generate JWT token for the new user
            String token = authTokenUtil.generateToken(newUser.getEmail());
            logger.info("User {} registered successfully.", registerRequest.getEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.USER_REGISTERED, token));
        } catch (IllegalArgumentException ex) {
            logger.warn("Registration failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error during registration for email: {}", registerRequest.getEmail(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, Constants.Errors.INTERNAL_SERVER_ERROR));
        }
    }
}
