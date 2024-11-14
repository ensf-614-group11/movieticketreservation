package com.acmeplex.movieticketreservation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to add a custom header to every HTTP response.
 * Adds a `X-Custom-Header` with a static value.
 * Also logs the process of adding the header and handles potential errors.
 */
@Component
public class CustomHeaderFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomHeaderFilter.class);

    /**
     * Adds a custom header to the response and logs the action.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing the request
     * @throws ServletException if the request processing fails
     * @throws IOException if an I/O error occurs while writing the response
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Add a custom header to the response
            response.addHeader("X-Custom-Header", "AcmePlexApp v1.0");

            // Log the addition of the custom header
            logger.info("Added custom header to response: X-Custom-Header = AcmePlexApp v1.0");

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Log any exception that occurs during the filtering process
            logger.error("Error while adding custom header to response: {}", e.getMessage(), e);

            // Respond with an internal server error if something goes wrong
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred while processing the request.");
        }
    }
}
