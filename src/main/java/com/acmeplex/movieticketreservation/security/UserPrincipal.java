package com.acmeplex.movieticketreservation.security;

import java.security.Principal;

/**
 * UserPrincipal represents a user identity with a name, used for authentication purposes.
 * This class implements the Principal interface, which allows it to integrate with Java security APIs.
 */
public class UserPrincipal implements Principal {

    private String name; // The name of the user (typically the username)

    /**
     * Constructs a UserPrincipal with the specified name.
     *
     * @param name the name of the user
     */
    public UserPrincipal(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this principal, which identifies the user.
     *
     * @return the name of the user
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the UserPrincipal.
     *
     * @return the name of the user as a string
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this UserPrincipal to another object for equality.
     * Two UserPrincipal objects are considered equal if they have the same name.
     *
     * @param obj the object to compare with
     * @return true if both objects have the same name; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check for reference equality
        if (obj == null || getClass() != obj.getClass()) return false; // Check for null or class mismatch
        UserPrincipal that = (UserPrincipal) obj;
        return name.equals(that.name); // Compare names for equality
    }

    /**
     * Returns the hash code for this UserPrincipal, based on the name.
     * This ensures consistent behavior when used in hashed collections.
     *
     * @return the hash code for the name of the user
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
