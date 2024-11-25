package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexMembership;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles post-registration tasks, such as membership creation.
 */
@Service
public class RegistrationPostProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationPostProcessingService.class);

    private final AcmePlexMembershipService membershipService;
    private final AcmePlexUserProfileService userProfileService;

    public RegistrationPostProcessingService(AcmePlexMembershipService membershipService,
                                             AcmePlexUserProfileService userProfileService) {
        this.membershipService = membershipService;
        this.userProfileService = userProfileService;
    }

    /**
     * Handles post-registration tasks for a newly registered user.
     *
     * @param user The registered user.
     */
    @Transactional
    public void handlePostRegistration(AcmePlexUser user) {
        logger.info("Handling post-registration tasks for user: {}", user.getEmail());

        // Create and associate membership
        AcmePlexMembership membership = membershipService.createMembership(user.getDateRegistered());
        user.setMembership(membership);

        // Save the updated user with membership
        userProfileService.save(user);
        logger.debug("Membership created and associated with user: {}", user.getEmail());
    }
}
