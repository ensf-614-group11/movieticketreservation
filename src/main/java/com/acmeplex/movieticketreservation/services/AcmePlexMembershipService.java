package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexMembership;

import java.util.Date;

public class AcmePlexMembershipService {

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
