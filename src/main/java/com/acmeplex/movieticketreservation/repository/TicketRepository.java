package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    public Optional<Ticket> findById(Long ticketId);

}
