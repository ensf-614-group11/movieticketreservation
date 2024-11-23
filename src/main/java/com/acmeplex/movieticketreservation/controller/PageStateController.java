package com.acmeplex.movieticketreservation.controller;

import com.acmeplex.movieticketreservation.security.UserPrincipal;
import com.acmeplex.movieticketreservation.security.AuthUserPrincipalService;
import com.acmeplex.movieticketreservation.dto.response.SharedPageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling both authenticated and unauthenticated access to shared pages.
 */
@RestController
public class PageStateController {

    private static final Logger logger = LoggerFactory.getLogger(PageStateController.class);

    private final AuthUserPrincipalService userPrincipalService;

    /**
     * Constructs the controller with necessary dependencies.
     *
     * @param userPrincipalService the service to handle user principal logic
     */
    public PageStateController(AuthUserPrincipalService userPrincipalService) {
        this.userPrincipalService = userPrincipalService;
    }

    /**
     * Handles an endpoint accessible by both authenticated and unauthenticated users.
     *
     * @return a response with data tailored to the user's authentication state
     */
    @GetMapping("/shared-page")
    public ResponseEntity<SharedPageResponse> handleSharedPage() {
        UserPrincipal userPrincipal = userPrincipalService.getCurrentUserPrincipal();

        if (userPrincipal != null) {
            logger.info("Authenticated user accessed shared-page: {}", userPrincipal.getUsername());
            return ResponseEntity.ok(new SharedPageResponse(
                    "Welcome back!",
                    userPrincipal.getId(),
                    userPrincipal.getUsername()
            ));
        } else {
            logger.info("Unauthenticated user accessed shared-page.");
            return ResponseEntity.ok(new SharedPageResponse(
                    "Welcome to the shared page. Please log in for a personalized experience.",
                    null,
                    null
            ));
        }
    }
}
