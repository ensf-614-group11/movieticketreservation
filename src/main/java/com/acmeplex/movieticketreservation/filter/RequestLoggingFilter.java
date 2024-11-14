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
 * Filter to log details of incoming requests.
 * Logs the HTTP method, URI, and remote address for each request.
 * Handles errors during filtering and logs them for debugging purposes.
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    /**
     * Filters incoming requests and logs details such as HTTP method, URI, and remote address.
     * It also handles potential exceptions that could arise during filtering.
     *
     * @param request  the HTTP request to be processed
     * @param response the HTTP response to be processed
     * @param filterChain the filter chain that further processes the request and response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Log request details
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();

            // Log at INFO level for successful request processing
            logger.info("Incoming request - Method: {}, URI: {}, Remote address: {}", method, uri, remoteAddr);

            // Continue the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Log any errors that occur during the request processing
            logger.error("Error while processing request: {}", e.getMessage(), e);

            // Optionally set a custom error response status
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 - Internal Server Error
            response.getWriter().write("An error occurred while processing the request.");
        }
    }
}
