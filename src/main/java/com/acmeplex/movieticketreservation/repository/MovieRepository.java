package com.acmeplex.movieticketreservation.repository;

import com.acmeplex.movieticketreservation.model.AcmePlexCredit;
import com.acmeplex.movieticketreservation.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
