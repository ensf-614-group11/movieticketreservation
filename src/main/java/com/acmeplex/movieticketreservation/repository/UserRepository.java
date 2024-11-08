package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AcmePlexUser, Long> {
    Optional<AcmePlexUser> findByUsername(String username);
}
