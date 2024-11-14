package com.acmeplex.movieticketreservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Custom error controller to handle various HTTP errors (e.g., 404, 500)
 * and return user-friendly error messages. Also logs errors for debugging.
 */
@Controller
public class AcmePlexErrorController {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexErrorController.class);

    /**
     * Handles errors based on the HTTP status code and logs the error.
     *
     * @param request the HTTP request containing error details
     * @return a user-friendly error message
     */
    @GetMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage;

        // Log the request URI, client's IP address, and user-agent for better traceability
        String requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        logger.info("Error occurred while accessing URI: {} from IP: {} with User-Agent: {}", requestURI, clientIP, userAgent);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // Handle different status codes using HttpStatus enum constants
            switch (HttpStatus.valueOf(statusCode)) {
                case NOT_FOUND:
                    errorMessage = "404 - Page Not Found";
                    break;
                case INTERNAL_SERVER_ERROR:
                    errorMessage = "500 - Internal Server Error";
                    break;
                case FORBIDDEN:
                    errorMessage = "403 - Forbidden";
                    break;
                case BAD_REQUEST:
                    errorMessage = "400 - Bad Request";
                    break;
                default:
                    errorMessage = "An unexpected error occurred.";
            }

            // Log the error with the status code and message
            logger.error("Error occurred with status code: {} - {}", statusCode, errorMessage);
        } else {
            // Default error message if no status code is found
            errorMessage = "An error occurred. Please try again or contact support.";
            logger.error("Error occurred with no status code provided.");
        }

        return errorMessage;
    }
}
