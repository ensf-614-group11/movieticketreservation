package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.SharedPageResponse;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class SharedStateService {

    private final AuthUserPrincipalService authUserPrincipalService;

    public SharedStateService(AuthUserPrincipalService authUserPrincipalService) {
        this.authUserPrincipalService = authUserPrincipalService;
    }

    public SharedPageResponse getSharedPageResponse() {
        UserPrincipal userPrincipal = authUserPrincipalService.getCurrentUserPrincipal();
        if (userPrincipal != null) {
            return new SharedPageResponse(
                    "Welcome back, " + userPrincipal.getUsername() + "!",
                    userPrincipal.getId(),
                    userPrincipal.getUsername()
            );
        } else {
            return new SharedPageResponse(
                    "Welcome to our platform. Please log in for a personalized experience.",
                    null,
                    null
            );
        }
    }
}
