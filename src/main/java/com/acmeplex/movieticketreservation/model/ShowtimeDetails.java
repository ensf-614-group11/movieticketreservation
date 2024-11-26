package com.acmeplex.movieticketreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
/**
 * This class is a helper class that encapsulates the essential details of a movie showtime,
 * which are needed to be included in a Ticket object. The purpose of this class is to avoid
 * issues related to nested object serialization and the complexity of handling nested
 * relationships between entities when dealing with the Ticket object.
 *
 * In particular, this class extracts the relevant data from the Showtime entity (date and time,
 * movie title, and theatre name) and provides simplified, serialized representations of those
 * fields. By doing so, it ensures that only the necessary information is serialized and passed
 * without causing potential circular dependencies or deep object nesting issues.
 *
 *
 * @see Showtime
 * @see Ticket
 */

@JsonSerialize
public class ShowtimeDetails {
    /** The date and time of the movie showtime. */
    LocalDateTime dateAndTime;
    /** The title of the movie being shown. */
    String movieTitle;
    /** The name of the theatre where the movie is being shown. */
    String theatre;

    /**
     * Constructs a new {@code ShowtimeDetails} object by extracting the relevant details
     * from the provided {@code Showtime} object. This avoids direct nesting of complex
     * relationships between the Ticket and Showtime entities.
     *
     * @param showtime the Showtime object containing the full details of the movie showtime.
     *                 The relevant details (date and time, movie title, and theatre name)
     *                 are extracted from this object.
     */
    public ShowtimeDetails(Showtime showtime) {
        this.dateAndTime = showtime.getDateAndTime();
        // Extract the movie title, checking if movie object is not null to avoid NullPointerException
        this.movieTitle = showtime.getMovie() != null ? showtime.getMovie().getTitle() : null;
        // Extract the theatre name from the theatre room, ensuring no null references.
        this.theatre = showtime.getTheatreRoom().getTheatre().getName();

    }
    /**
     * Returns the date and time of the movie showtime.
     *
     * @return the date and time of the showtime
     */
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }
    /**
     * Returns the title of the movie being shown in this showtime.
     *
     * @return the movie title, or null if no movie is available
     */
    public String getMovieTitle() {
        return movieTitle;
    }
    /**
     * Returns the name of the theatre where the movie is being shown.
     *
     * @return the name of the theatre
     */
    public String getTheatre() {
        return theatre;
    }


}
