package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.Movie;
import com.acmeplex.movieticketreservation.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }
}
