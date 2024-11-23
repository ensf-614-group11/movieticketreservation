package com.acmeplex.movieticketreservation.config.security;

import com.acmeplex.movieticketreservation.config.constants.Constants.Endpoints;

import java.util.List;

/**
 * Contains security endpoint configurations for public and protected routes.
 */
public class EndpointSecurityConfig {

    private EndpointSecurityConfig() {
        // Private constructor to prevent instantiation
    }

    /**
     * Public endpoints that do not require authentication.
     */
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            Endpoints.API_AUTH_LOGIN,
            Endpoints.API_AUTH_REGISTER,
            "/public/**", // Static files and public resources
            "/static/**",
            "/favicon.ico",
            Endpoints.API_ERROR,
            Endpoints.API_THEATRES,
            Endpoints.API_MOVIES,
            Endpoints.API_SHOWTIMES,
            Endpoints.API_AVAILABLE_SEATS);

    /**
     * Protected endpoints that require authentication.
     */
    public static final List<String> PROTECTED_ENDPOINTS = List.of(
            Endpoints.API_USER_PROFILE + "/**", // All subpaths under user profile
            // Endpoints.API_SHARED_PAGE // Shared page endpoint
    );
}
