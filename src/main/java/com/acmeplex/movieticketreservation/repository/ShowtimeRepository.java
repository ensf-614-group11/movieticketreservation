package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.*;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    Showtime findByTheatreRoomAndMovie(TheatreRoom crowfootRoom1, Movie avengers);

    Showtime findById(Long id);

    Showtime findByTheatreRoomAndMovie(TheatreRoom crowfootRoom1, Movie avengers);

    /**
     * Custom query to fetch showtimes by date, movie ID, and theatre ID.
     * Joins Showtime -> TheatreRoom -> Theatre to filter by theatre.
     */
    @Query("""
                        SELECT s FROM Showtime s
                        JOIN TheatreRoom tr ON s.theatreRoom.id = tr.id
                        JOIN Theatre t ON tr.theatre.id = t.id
                        WHERE s.dateAndTime >= :startOfDay AND s.dateAndTime < :endOfDay
                          AND (:movieId IS NULL OR s.movie.id = :movieId)
                          AND (:theatreId IS NULL OR t.id = :theatreId)
                    """)
    List<Showtime> findByDateMovieAndTheatre(
                    @Param("startOfDay") LocalDateTime startOfDay,
                    @Param("endOfDay") LocalDateTime endOfDay,
                    @Param("movieId") Long movieId,
                    @Param("theatreId") Long theatreId);
}
