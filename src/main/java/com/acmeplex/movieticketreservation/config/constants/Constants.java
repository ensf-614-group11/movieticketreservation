package com.acmeplex.movieticketreservation.config.constants;

/**
 * Shared constants used across the application, including API endpoints, error messages, and success messages.
 */
public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    /**
     * API Endpoints grouped by functional areas.
     */
    public static final class Endpoints {
        private Endpoints() {
            // Private constructor to prevent instantiation
        }

        // Authentication Endpoints
        public static final String API_AUTH = "/api/auth";
        public static final String API_AUTH_LOGIN = API_AUTH + "/login";
        public static final String API_AUTH_REGISTER = API_AUTH + "/register";

        // User Profile Endpoints
        public static final String API_USER_PROFILE = "/api/user/profile";
        public static final String API_USER_PROFILE_BILLING_ADDRESS = API_USER_PROFILE + "/billing-address";
        public static final String API_USER_PROFILE_PAYMENT_TYPE = API_USER_PROFILE + "/payment-type";

        // Shared Page Endpoint
        public static final String API_SHARED_PAGE = "/api/shared-page";

        // Error Endpoint
        public static final String API_ERROR = "/error";
    }

    /**
     * Error message constants.
     */
    public static final class Errors {
        private Errors() {
            // Private constructor to prevent instantiation
        }

        public static final String USER_NOT_FOUND = "User not found with email: ";
        public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";
        public static final String FORBIDDEN = "You do not have permission to access this resource.";
        public static final String BAD_REQUEST = "The request was invalid. Please check your input.";
        public static final String NOT_FOUND = "The requested page was not found.";
    }

    /**
     * Success message constants.
     */
    public static final class Success {
        private Success() {
            // Private constructor to prevent instantiation
        }

        public static final String PROFILE_UPDATED = "Profile updated successfully";
        public static final String USER_REGISTERED = "User registered successfully";
        public static final String USER_LOGGED_IN = "Login successful";
    }
}
