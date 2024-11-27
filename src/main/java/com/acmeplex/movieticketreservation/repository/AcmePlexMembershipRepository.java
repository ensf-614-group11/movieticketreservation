package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexMembership;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AcmePlexMembershipRepository extends JpaRepository<AcmePlexMembership, Long> {

    @Query("SELECT m FROM AcmePlexMembership m WHERE m.expiryDate < :currentDate AND m.renewalStatus = true")
    List<AcmePlexMembership> findExpiredMembershipsUpForRenewal(Date currentDate);

    @Query("SELECT m.acmePlexUser FROM AcmePlexMembership m WHERE m.id = :membershipId")
    AcmePlexUser findUserByMembershipId(Long membershipId);

    @Query("SELECT m FROM AcmePlexMembership m WHERE m.expiryDate < :currentDate AND m.renewalStatus = false")
    List<AcmePlexMembership> findExpiredMembershipsWithoutRenewal(Date currentDate);

    @Query("SELECT m FROM AcmePlexMembership m WHERE m.expiryDate = :lastWeekDate")
    List<AcmePlexMembership> findMembershipsExpiringInOneWeek(@Param("lastWeekDate") Date lastWeekDate);

}
