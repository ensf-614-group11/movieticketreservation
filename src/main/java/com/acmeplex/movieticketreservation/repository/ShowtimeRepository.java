package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    Showtime findByTheatreRoomAndMovie(TheatreRoom crowfootRoom1, Movie avengers);

    Showtime findById(Long id);
}
