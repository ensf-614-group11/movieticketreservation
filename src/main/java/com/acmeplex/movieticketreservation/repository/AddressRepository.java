package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
