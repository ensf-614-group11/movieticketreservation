package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AcmePlexErrorController {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexErrorController.class);

    @GetMapping(Constants.Endpoints.API_ERROR)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        String requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        logger.info("Error occurred while accessing URI: {} from IP: {} with User-Agent: {}", requestURI, clientIP, userAgent);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.resolve(statusCode);

            String errorMessage = switch (httpStatus) {
                case NOT_FOUND -> Constants.Errors.NOT_FOUND;
                case INTERNAL_SERVER_ERROR -> Constants.Errors.INTERNAL_SERVER_ERROR;
                case FORBIDDEN -> Constants.Errors.FORBIDDEN;
                case BAD_REQUEST -> Constants.Errors.BAD_REQUEST;
                default -> "An unexpected error occurred.";
            };

            logger.error("Error occurred with status code: {} - {}", statusCode, httpStatus != null ? httpStatus.getReasonPhrase() : "Unknown");

            response.put("status", statusCode);
            response.put("error", httpStatus != null ? httpStatus.getReasonPhrase() : "Unknown Error");
            response.put("message", errorMessage);
            response.put("path", requestURI);
        } else {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", "Internal Server Error");
            response.put("message", Constants.Errors.INTERNAL_SERVER_ERROR);
            response.put("path", requestURI);
            logger.error("Error occurred with no status code provided.");
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}
