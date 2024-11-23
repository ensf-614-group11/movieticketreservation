package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.MovieResponse;
import com.acmeplex.movieticketreservation.model.Movie;
import com.acmeplex.movieticketreservation.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<MovieResponse> getAllMovies() {
    return movieRepository.findAll().stream()
        .map(movie -> new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getLength(),
            movie.getReleaseDate(),
            movie.getDescription()))
        .collect(Collectors.toList());
  }
}
