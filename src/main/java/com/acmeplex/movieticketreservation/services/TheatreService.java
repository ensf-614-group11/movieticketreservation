package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.response.TheatreResponse;
import com.acmeplex.movieticketreservation.model.Theatre;
import com.acmeplex.movieticketreservation.repository.TheatreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreService {
  private final TheatreRepository theatreRepository;

  public TheatreService(TheatreRepository theatreRepository) {
    this.theatreRepository = theatreRepository;
  }

  public List<TheatreResponse> getAllTheatres() {
    return theatreRepository.findAll().stream()
        .map(theatre -> new TheatreResponse(
            theatre.getId(),
            theatre.getName(),
            theatre.getLocation()))
        .collect(Collectors.toList());
  }
}
