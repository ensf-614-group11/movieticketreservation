package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.model.Movie;
import com.acmeplex.movieticketreservation.model.Seat;
import com.acmeplex.movieticketreservation.model.TheatreRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    Seat findByTheatreRoomAndRowNumberAndSeatNumber(TheatreRoom crowfootRoom1, int i, int i1);
}
