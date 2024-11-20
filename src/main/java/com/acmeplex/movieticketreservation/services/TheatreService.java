package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.Theatre;
import com.acmeplex.movieticketreservation.repository.TheatreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {
  private final TheatreRepository theatreRepository;

  public TheatreService(TheatreRepository theatreRepository) {
    this.theatreRepository = theatreRepository;
  }

  public List<Theatre> getAllTheatres() {
    return theatreRepository.findAll();
  }
}
