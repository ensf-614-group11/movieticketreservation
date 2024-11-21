package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.auth.context.AuthStrategyContext;
import com.acmeplex.movieticketreservation.auth.strategy.AuthStrategy;
import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.*;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for handling authentication and registration using strategy patterns.
 */
@RestController
@RequestMapping(Constants.Endpoints.API_AUTH)
public class AuthController {

    private final AuthStrategyContext strategyContext;

    public AuthController(AuthStrategyContext strategyContext) {
        this.strategyContext = strategyContext;
    }

    /**
     * Handles user login with a specific authentication strategy.
     *
     * @param strategyType the type of strategy to use for login
     * @param request      the login request containing user credentials
     * @return a ResponseEntity containing the login result
     */
    @PostMapping(Constants.Endpoints.API_AUTH_LOGIN)
    public ResponseEntity<ApiResponse<String>> login(@RequestParam String strategyType,
                                                     @RequestBody @Valid DefaultLoginRequest request) {
        AuthStrategy<DefaultLoginRequest> strategy = strategyContext.getStrategy(strategyType);
        return strategy.execute(request);
    }

    /**
     * Handles user registration with a specific registration strategy.
     *
     * @param strategyType the type of strategy to use for registration
     * @param requests     an array of request objects needed for registration
     * @return a ResponseEntity containing the registration result
     */
    @PostMapping(Constants.Endpoints.API_AUTH_REGISTER)
    public ResponseEntity<ApiResponse<String>> register(@RequestParam String strategyType,
                                                        @RequestBody @Valid Object[] requests) {
        AuthStrategy<Object[]> strategy = strategyContext.getStrategy(strategyType);
        return strategy.execute(requests);
    }
}
