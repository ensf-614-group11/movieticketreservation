package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.LoginRequest;
//import com.acmeplex.movieticketreservation.dto.RegisterRequest;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
//import com.acmeplex.movieticketreservation.service.UserService; // Import your UserService for user handling
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil; // Inject AuthTokenUtil

    public AuthController(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // Log the request URI
        String requestUri = request.getRequestURI();
        logger.info("Login request received at URI: {}", requestUri);

        // Check for encoded newlines (%0A) in the username and password fields
        if (loginRequest.getUsername().contains("%0A") || loginRequest.getPassword().contains("%0A")) {
            logger.warn("Request contains encoded newline (%0A) in username or password.");
            return ResponseEntity.badRequest().body("Encoded newline (%0A) is not allowed in credentials.");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Generate token after successful authentication
            String token = authTokenUtil.generateToken(loginRequest.getUsername());
            logger.info("User {} authenticated successfully.", loginRequest.getUsername());
            return ResponseEntity.ok(token); // Return the JWT token
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Uncomment when UserService is implemented
    /*
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // Implement user registration logic
        boolean isRegistered = userService.registerUser(registerRequest); // Add this method in UserService

        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
    }
    */
}