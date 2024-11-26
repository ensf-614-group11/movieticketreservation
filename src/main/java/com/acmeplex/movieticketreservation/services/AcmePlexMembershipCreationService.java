package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexMembership;
import com.acmeplex.movieticketreservation.repository.AcmePlexMembershipRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AcmePlexMembershipCreationService {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexMembershipCreationService.class);
    private final AcmePlexMembershipRepository membershipRepository;

    public AcmePlexMembershipCreationService(AcmePlexMembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }


    /**
     * Creates a new membership for a given registration date.
     *
     * @param dateRegistered The date the user registered.
     * @return The newly created membership.
     */
    @Transactional
    public AcmePlexMembership createMembership(Date dateRegistered) {
        Date expiryDate = calculateExpirationDate(dateRegistered);
        AcmePlexMembership membership = new AcmePlexMembership(dateRegistered, expiryDate);
        membershipRepository.save(membership);
        logger.debug("Created and saved membership with ID: {}", membership.getId());
        return membership;
    }

    /**
     * Activates the membership.
     *
     * @param membership the membership to activate
     */
    public void activateMembership(AcmePlexMembership membership) {
        membership.setActiveMembership(true);
        // Add additional business logic if required, e.g., logging or notification
    }

    /**
     * Deactivates the membership.
     *
     * @param membership the membership to deactivate
     */
    public void deactivateMembership(AcmePlexMembership membership) {
        membership.setActiveMembership(false);
        // Add additional business logic if required, e.g., logging or notification
    }

    /**
     * Activates the renewal status.
     *
     * @param membership the membership to activate renewal status for
     */
    public void activateRenewalStatus(AcmePlexMembership membership) {
        membership.setRenewalStatus(true);
        // Add additional business logic if required
    }

    /**
     * Deactivates the renewal status.
     *
     * @param membership the membership to deactivate renewal status for
     */
    public void deactivateRenewalStatus(AcmePlexMembership membership) {
        membership.setRenewalStatus(false);
        // Add additional business logic if required
    }

    /**
     * Calculates the expiration date for a membership, e.g., one year from registration.
     *
     * @param dateRegistered The date the user registered.
     * @return The expiration date.
     */
    private Date calculateExpirationDate(Date dateRegistered) {
        // Example logic for a one-year membership
        return new Date(dateRegistered.getTime() + 365L * 24 * 60 * 60 * 1000); // Add 1 year in milliseconds
    }

    /**
     * Processes the renewal of an AcmePlex membership based on its current state.
     *
     * @param membership the membership to renew
     */
    public void processMembershipRenewal(AcmePlexMembership membership) {
        if (membership.isActiveMembership() && membership.isRenewalStatus()) {
            // Case 1: Active membership and renewal status is true - do nothing
            System.out.println("Membership is active and renewal status is true. No action required.");
        } else if (!membership.isActiveMembership()) {
            // Case 2: Inactive membership - process payment and reactivate
            // Example logic for payment processing
            System.out.println("Processing payment for membership renewal...");
            membership.setActiveMembership(true); // Reactivate membership
            membership.setRenewalDate(new Date()); // Set renewal date to current date
            membership.setRenewalStatus(true); // Set renewal status to true
            System.out.println("Membership has been renewed and reactivated.");
        } else if (membership.isActiveMembership() && !membership.isRenewalStatus()) {
            // Case 3: Active membership but renewal status is false - set renewal status to true
            membership.setRenewalStatus(true);
            System.out.println("Membership is active, but renewal status was false. Renewal status is now set to true.");
        }
    }



}
