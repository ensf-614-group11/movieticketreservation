package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.model.AcmePlexMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcmePlexMembershipRepository extends JpaRepository<AcmePlexMembership, Long> {
}
