package com.acmeplex.movieticketreservation.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaymentTypeDateUtil {

    private static final DateTimeFormatter MMYY_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    /**
     * Converts a string in MM/YY format to LocalDate.
     * @param mmyy the MM/YY formatted string.
     * @return the converted LocalDate.
     * @throws IllegalArgumentException if the date format is invalid.
     */
    public static LocalDate parseMMYYToLocalDate(String mmyy) {
        try {
            return LocalDate.parse("01/" + mmyy, DateTimeFormatter.ofPattern("dd/MM/yy"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiration date format. Expected MM/YY.", e);
        }
    }
}
