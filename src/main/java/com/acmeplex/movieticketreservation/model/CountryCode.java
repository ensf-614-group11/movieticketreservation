package com.acmeplex.movieticketreservation.model;

public enum CountryCode {
    US, // United States
    CA;  // Canada

    /**
     * Converts a string to a CountryCode enum.
     * Throws IllegalArgumentException if the string does not match a valid CountryCode.
     *
     * @param code the country code string to convert
     * @return CountryCode enum
     */
    public static CountryCode fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Country code cannot be null or blank.");
        }

        try {
            return CountryCode.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid country code: " + code);
        }
    }
}
