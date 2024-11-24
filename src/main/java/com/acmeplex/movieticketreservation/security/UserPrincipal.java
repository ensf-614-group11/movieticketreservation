package com.acmeplex.movieticketreservation.security;

import com.acmeplex.movieticketreservation.model.CountryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the authenticated user's identity, implementing {@link UserDetails} for integration with Spring Security.
 * Contains essential user information such as id, email, first name, and last name.
 */
public class UserPrincipal implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class); // Logger instance

    private final Long id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;

    /**
     * Constructs a new {@code UserPrincipal} with the specified user details.
     *
     * @param id          the unique identifier for the user
     * @param email       the email address of the user, used as a unique identifier for authentication
     * @param password    the encrypted password of the user
     * @param firstName   the user's first name
     * @param lastName    the user's last name
     */
    public UserPrincipal(Long id,
                         String email,
                         String password,
                         String firstName,
                         String lastName,
                         CountryCode countryCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryCode = countryCode;
        logger.info("UserPrincipal object created for email: {}", email);
    }

    public Long getId() {
        return id;  // Return the Long id instead of int
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public CountryCode getCountryCode() {
        return countryCode; // Read-only
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns a collection of authorities granted to the user.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the user's authorities
     */
    @Override
    public List<GrantedAuthority> getAuthorities() {
        // Assuming no roles for now, returning an empty list or you can add roles here
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }
}
