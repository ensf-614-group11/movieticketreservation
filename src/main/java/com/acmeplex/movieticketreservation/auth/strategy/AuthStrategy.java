package com.acmeplex.movieticketreservation.auth.strategy;

import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

/**
 * Common interface for authentication strategies.
 */
public interface AuthStrategy<T> {
    ResponseEntity<ApiResponse<String>> execute(T request);
}
