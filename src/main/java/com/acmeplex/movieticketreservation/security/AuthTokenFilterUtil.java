package com.acmeplex.movieticketreservation.security;

import com.acmeplex.movieticketreservation.services.AcmeUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to authenticate and validate users using JWT tokens.
 * It extracts the email from the JWT and sets the authentication in the security context if valid.
 */
@Component
public class AuthTokenFilterUtil extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilterUtil.class);

    @Autowired
    private AuthTokenUtil authTokenUtil; // Your JWT utility class

    @Autowired
    private AcmeUserDetailsService userDetailsService; // Your custom user details service

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, java.io.IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix
                email = authTokenUtil.extractEmail(jwt); // Extract email from the JWT

                logger.info("Extracted email from JWT: {}", email);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // If the user is valid and not already authenticated
                UserDetails userDetails = userDetailsService.loadUserByUsername(email); // Load user by email

                if (authTokenUtil.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    logger.info("Successfully authenticated user: {}", email);
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Error occurred while processing the authentication filter for email {}: {}", email, e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set appropriate HTTP status for unauthorized access
            response.getWriter().write("Authentication failed: Invalid token or user not found.");
        }
    }
}
