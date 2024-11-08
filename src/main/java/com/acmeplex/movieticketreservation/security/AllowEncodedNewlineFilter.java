package com.acmeplex.movieticketreservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AllowEncodedNewlineFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        // Allow requests containing encoded newlines (%0A)
        if (requestUri.contains("%0A")) {
            // Allow the request to continue without blocking
            filterChain.doFilter(request, response);
            return;
        }

        // Continue with the regular filter chain
        filterChain.doFilter(request, response);
    }
}
