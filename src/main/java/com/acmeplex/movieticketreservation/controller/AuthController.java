package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.LoginRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
import com.acmeplex.movieticketreservation.services.AcmeUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Validated
@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil;
    private final AcmeUserDetailsService userService;

    /**
     * Constructs the AuthController with required dependencies.
     *
     * @param authenticationManager the authentication manager for user authentication
     * @param authTokenUtil         utility for JWT token generation and validation
     * @param userService           the service handling user registration
     */
    public AuthController(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil, AcmeUserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
        this.userService = userService;
    }

    /**
     * Authenticates a user and generates a JWT token if successful.
     * Caches the user details after successful login.
     *
     * @param loginRequest the login request containing username and password
     * @param request      the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the JWT token on successful authentication
     */
    @PostMapping("/auth/login")
    @Cacheable(value = "userProfileCache", key = "#loginRequest.getEmail()") // Cache the user by email
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // Log request details
        logger.info("Login attempt for email: {} from IP: {}", loginRequest.getEmail(), request.getRemoteAddr());

        // Security restrictions on incoming requests
        if (hasInvalidCharacters(loginRequest.getEmail()) || hasInvalidCharacters(loginRequest.getPassword())) {
            logger.warn("Detected disallowed characters in credentials.");
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid characters in credentials"));
        }

        try {
            // Authenticate the user with the provided credentials
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Generate token after successful authentication
            String token = authTokenUtil.generateToken(loginRequest.getEmail());
            logger.info("User {} authenticated successfully.", loginRequest.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", token));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user {}: {}", loginRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid credentials"));
        } catch (Exception e) {
            logger.error("Unexpected error during login attempt for user {}: {}", loginRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Internal server error"));
        }
    }

    /**
     * Registers a new user if the email is not already in use.
     * Caches the newly registered user.
     *
     * @param registerRequest the registration request containing user details
     * @return a ResponseEntity containing an ApiResponse with a success message on successful registration
     */
    @PostMapping("/auth/register")
    @CachePut(value = "userProfileCache", key = "#registerRequest.getEmail()")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Register the user
            AcmePlexUser newUser = userService.registerUser(registerRequest);
            logger.info("User registered with email: {}", newUser.getEmail());

            // Generate JWT token
            String token = authTokenUtil.generateToken(newUser.getEmail());

            // Return success response with token
            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", token));
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed for email {}: {}", registerRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during registration for email {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Internal server error"));
        }
    }


    /**
     * Checks if the input string contains disallowed characters.
     *
     * @param input the input string to check
     * @return true if disallowed characters are found, false otherwise
     */
    private boolean hasInvalidCharacters(String input) {
        String[] disallowedCharacters = {"%0A", "%0D", "<", ">", "%3C", "%3E", "'", "--", ";", "%27", " ", "\n", "\t"};
        for (String charStr : disallowedCharacters) {
            if (input.contains(charStr)) {
                logger.warn("Disallowed characters found in input: {}", input);
                return true;
            }
        }
        return false;
    }
}
