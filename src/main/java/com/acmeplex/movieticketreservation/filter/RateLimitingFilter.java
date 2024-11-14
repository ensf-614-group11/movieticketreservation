package com.acmeplex.movieticketreservation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Simple rate limiting filter to restrict excessive requests from a single client IP.
 * Allows up to 10 requests per minute per client IP.
 * Handles errors and logs rate-limiting events.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private final ConcurrentHashMap<String, RateLimit> rateLimitMap = new ConcurrentHashMap<>();

    /**
     * Filters incoming requests and applies rate-limiting logic.
     * Logs events related to rate-limiting and handles errors.
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

        String clientIp = request.getRemoteAddr();
        RateLimit rateLimit = rateLimitMap.computeIfAbsent(clientIp, k -> new RateLimit(MAX_REQUESTS_PER_MINUTE));

        try {
            if (!rateLimit.allowRequest()) {
                // Log the rate limit exceed event
                logger.warn("Rate limit exceeded for IP: {}", clientIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests. Please try again later.");
                return;
            }

            // Proceed with the next filter in the chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Log the exception if an error occurs during the rate limiting process
            logger.error("Error processing rate limiting filter for IP: {}", clientIp, e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("An error occurred while processing your request.");
        }
    }

    /**
     * Internal class to track the rate limit status for each IP.
     */
    private static class RateLimit {

        private int remainingRequests;
        private long lastRefillTimestamp;

        public RateLimit(int maxRequests) {
            this.remainingRequests = maxRequests;
            this.lastRefillTimestamp = System.nanoTime();
        }

        /**
         * Allows a request if the rate limit allows.
         * Refills the remaining requests if a minute has passed.
         *
         * @return true if the request is allowed, false if the limit is exceeded
         */
        synchronized boolean allowRequest() {
            refill();

            if (remainingRequests > 0) {
                remainingRequests--;
                return true;
            }
            return false;
        }

        /**
         * Refills the request count if a minute has passed since the last refill.
         */
        private void refill() {
            long now = System.nanoTime();
            long elapsed = TimeUnit.NANOSECONDS.toMinutes(now - lastRefillTimestamp);
            if (elapsed > 0) {
                remainingRequests = MAX_REQUESTS_PER_MINUTE;
                lastRefillTimestamp = now;
            }
        }
    }
}
