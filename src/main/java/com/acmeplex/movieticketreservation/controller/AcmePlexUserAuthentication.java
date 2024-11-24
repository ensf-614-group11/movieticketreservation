package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.dto.request.DefaultLoginRequest;
import com.acmeplex.movieticketreservation.dto.request.DefaultRegistrationRequest;
import com.acmeplex.movieticketreservation.dto.response.*;
import com.acmeplex.movieticketreservation.facade.RegistrationFacade;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AcmePlexUserAuthentication {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexUserAuthentication.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil;
    private final RegistrationFacade registrationFacade;

    public AcmePlexUserAuthentication(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil, RegistrationFacade registrationFacade) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
        this.registrationFacade = registrationFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DefaultLoginRequest request) {
        logger.info("Login request received for email: {}", request.getEmail());

        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = authTokenUtil.generateToken(request.getEmail());
        logger.info("User {} authenticated successfully.", request.getEmail());

        AuthenticationResponse response = new AuthenticationResponse(token, request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DefaultRegistrationRequest request) {
        logger.info("Registration request received for email: {}", request.getUser().getEmail());

        AcmePlexUser newUser = registrationFacade.registerUser(
                request.getUser(),
                request.getBillingAddress(),
                request.getPaymentType()
        );

        String token = authTokenUtil.generateToken(newUser.getEmail());
        logger.info("User {} registered successfully.", request.getUser().getEmail());

        AuthenticationResponse response = new AuthenticationResponse(token, newUser.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
    }
}
