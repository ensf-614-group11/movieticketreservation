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
        public static final String API_AUTH_LOGIN = API_AUTH + "/login/**";
        public static final String API_AUTH_REGISTER = API_AUTH + "/register/**";

        // User Profile Endpoints
        public static final String API_USER_PROFILE = "/api/user/profile";
        public static final String API_USER_PROFILE_GET = API_USER_PROFILE + "/get";
        public static final String API_USER_PROFILE_UPDATE = API_USER_PROFILE + "/update";

        // Billing Address Endpoints
        public static final String API_BILLING_ADDRESS = "/api/user/profile/billing-address";
        public static final String API_BILLING_ADDRESS_UPDATE = API_BILLING_ADDRESS;

        // Payment Type Endpoints
        public static final String API_PAYMENT_TYPE = "/api/user/profile/payment-type";
        public static final String API_PAYMENT_TYPE_CREATE = API_PAYMENT_TYPE;
        public static final String API_PAYMENT_TYPE_SET_ACTIVE = API_PAYMENT_TYPE + "/set-active";
        public static final String API_PAYMENT_TYPE_GET = API_PAYMENT_TYPE + "/list";
        public static final String API_PAYMENT_TYPE_MANAGE = "/api/user/profile/payment-type/manage";

        // Ticket cancellation Endpoints
        public static final String API_TICKET_CANCELLATION = "/api/cancel-ticket";
        public static final String API_TICKET_CANCELLATION_POST_REGISTERED = API_TICKET_CANCELLATION +"/post/registered";
        public static final String API_TICKET_CANCELLATION_POST_ORDINARY = API_TICKET_CANCELLATION +"/post/ordinary";
        public static final String API_TICKET_CANCELLATION_GET = API_TICKET_CANCELLATION + "/get";

        // Error Endpoints
        public static final String API_ERROR = "/error";

        // Theatre Endpoints
        public static final String API_THEATRES = "/api/theatres";
        public static final String API_MOVIES = "/api/movies";
        public static final String API_SHOWTIMES = "/api/showtimes";
        public static final String API_AVAILABLE_SEATS = "/api/availableSeats/{showtimeId}";
    }


    /**
     * Error message constants.
     */
    public static final class Errors {



        private Errors() {
            // Private constructor to prevent instantiation
        }

        public static final String CONFLICT = "A conflict occurred. The resource already exists or cannot be modified.";
        public static final String USER_NOT_FOUND = "User not found with email: ";
        public static final String BILLING_ADDRESS_NOT_FOUND = "No billing address found for user: ";
        public static final String PAYMENT_TYPE_NOT_FOUND = "Payment type not found with ID: ";
        public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";
        public static final String FORBIDDEN = "You do not have permission to access this resource.";
        public static final String BAD_REQUEST = "The request was invalid. Please check your input.";
        public static final String NOT_FOUND = "The requested page was not found.";
        public static final String GENERIC_ERROR = "An error occurred while processing the request.";
        public static final String TICKET_NOT_FOUND = "Ticket not found with ID: ";
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
        public static final String PAYMENT_TYPE_CREATED = "Payment type created successfully";
        public static final String PAYMENT_TYPE_UPDATED = "Payment type updated successfully";
        public static final String BILLING_ADDRESS_UPDATED = "Billing address updated successfully";
        public static final String PAYMENT_TYPES_FETCHED = "User payment types retrieved successfully";
    }
}
