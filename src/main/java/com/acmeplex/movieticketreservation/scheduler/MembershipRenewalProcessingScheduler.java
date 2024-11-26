package com.acmeplex.movieticketreservation.scheduler;

import com.acmeplex.movieticketreservation.services.AcmePlexMembershipRenewalService;
import org.springframework.scheduling.annotation.Scheduled;

public class MembershipRenewalProcessingScheduler {
    private final AcmePlexMembershipRenewalService renewalService;

    public MembershipRenewalProcessingScheduler(AcmePlexMembershipRenewalService renewalService) {
        this.renewalService = renewalService;
    }

    // Schedule to run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void renewMembershipsDaily() {
        renewalService.renewExpiredMemberships();
    }

    // Schedule to run every day at 1 AM
    @Scheduled(cron = "0 0 1 * * ?")
    public void deactivateNonRenewingMembershipsDaily() {
        renewalService.deactivateNonRenewingMemberships();
    }
}
