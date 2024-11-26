package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.BillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.PaymentTypeRequest;
import com.acmeplex.movieticketreservation.model.*;
import com.acmeplex.movieticketreservation.repository.AcmePlexMembershipRepository;
import com.acmeplex.movieticketreservation.repository.ReceiptRepository;
import com.acmeplex.movieticketreservation.services.PaymentTypeService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AcmePlexMembershipRenewalService {
    private final AcmePlexUserProfileService userProfileService;
    private final AcmePlexMembershipRepository membershipRepository;
    private final ReceiptRepository receiptRepository;
    private final PaymentTypeService paymentTypeService;
    private final AddressService addressService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public AcmePlexMembershipRenewalService(
            AcmePlexUserProfileService acmePlexUserProfileService,
            AcmePlexMembershipRepository membershipRepository,
            ReceiptRepository receiptRepository,
            PaymentTypeService paymentTypeService,
            AddressService addressService,
            PaymentService paymentService,
            NotificationService notificationService) {
        this.userProfileService =  acmePlexUserProfileService;
        this.membershipRepository = membershipRepository;
        this.receiptRepository = receiptRepository;
        this.notificationService = notificationService;
        this.paymentTypeService = paymentTypeService;
        this.addressService = addressService;
        this.paymentService = paymentService;
    }

    public void renewExpiredMemberships() {
        // Fetch expired memberships eligible for renewal
        List<AcmePlexMembership> memberships = membershipRepository.findExpiredMembershipsUpForRenewal(new Date());

        for (AcmePlexMembership membership : memberships) {
            membership.setExpiryDate(new Date(membership.getExpiryDate().getTime() + 365L * 24 * 60 * 60 * 1000)); // Extend by 1 year
            membership.setRenewalDate(new Date()); // Set renewal date to today
            membership.setActiveMembership(true); // Mark as active again
            membershipRepository.save(membership);

            Payment renewalPayment = sendPaymentRenewalRequest(membership);
            AcmePlexUser user = membershipRepository.findUserByMembershipId(membership.getId());
            Receipt membershipReceipt = receiptRepository.findByPaymentId(renewalPayment.getId());
            notificationService.sendAnnualFeePaymentConfirmation(user, membershipReceipt);
        }
    }

    public void deactivateNonRenewingMemberships() {
        List<AcmePlexMembership> memberships = membershipRepository.findExpiredMembershipsWithoutRenewal(new Date());

        for (AcmePlexMembership membership : memberships) {
            membership.setActiveMembership(false);
            membershipRepository.save(membership);

            AcmePlexUser user = membershipRepository.findUserByMembershipId(membership.getId());
//            notificationService.sendMembershipDeactivationNotice(user);
            notificationService.removeObserver(user);
        }
    }

    /**
     *
     * Fetches the user associated with the given membership ID.
     *
     * @param membershipId the membership ID
     * @return the associated user
     */
    public AcmePlexUser getUserByMembershipId(Long membershipId) {
        AcmePlexUser user = membershipRepository.findUserByMembershipId(membershipId);
        return user;
    }

    private Payment sendPaymentRenewalRequest(AcmePlexMembership membership) {
        // Step 1: Get the associated user
        AcmePlexUser user = getUserByMembershipId(membership.getId());
        if (user == null) {
            throw new IllegalArgumentException("No user found for membership ID: " + membership.getId());
        }

        // Step 2: Find the active payment type for the user
        PaymentType activePayment = paymentTypeService.findActivePaymentTypeByUserId(user.getId());
        if (activePayment == null) {
            throw new IllegalArgumentException("No active payment method found for user: " + user.getEmail());
        }

        // Step 3: Determine the exact payment type (e.g., CreditCardPayment, PayPalPayment)
        Class<? extends PaymentType> paymentClass = activePayment.getClass();
        PaymentType specificPaymentType = paymentTypeService.getActivePaymentTypeAs(user.getId(), paymentClass);

        // Step 4: Get the billing address for the user
        Address billingAddress = addressService.findBillingAddressByUserId(user.getId());
        if (billingAddress == null) {
            throw new IllegalArgumentException("No billing address found for user: " + user.getEmail());
        }

        // Step 3: Send the payment request
        Payment renewalPayment = paymentService.processAcmePlexMembershipRenewal(membership.getRenewalToken());

        renewalPayment.setAcmePlexUser(user);
        user.getPayments().add(renewalPayment);
        paymentService.savePayment(renewalPayment); // Save the payment
        userProfileService.save(user);

        return renewalPayment;
    }
}
