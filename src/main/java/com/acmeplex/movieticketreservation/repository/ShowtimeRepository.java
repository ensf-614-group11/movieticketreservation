package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    Showtime findByTheatreRoomAndMovie(TheatreRoom crowfootRoom1, Movie avengers);

    /**
     * Custom query to fetch showtimes by date, movie ID, and theatre ID.
     * Joins Showtime -> TheatreRoom -> Theatre to filter by theatre.
     */
    @Query("""
                SELECT s FROM Showtime s
                JOIN TheatreRoom tr ON s.theatreRoom.id = tr.id
                JOIN Theatre t ON tr.theatre.id = t.id
                WHERE DATE(s.dateAndTime) = :date
                  AND s.movie.id = :movieId
                  AND t.id = :theatreId
            """)
    List<Showtime> findByDateMovieAndTheatre(
            @Param("date") String date,
            @Param("movieId") Long movieId,
            @Param("theatreId") Long theatreId);
}
