package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that implements {@link UserDetailsService} to load user-specific data for authentication.
 * Uses {@link AcmePlexUser} as the user model and provides a {@link UserPrincipal} with the user's details.
 * Also handles user registration and caching of user data.
 */
@Service
public class AcmeUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AcmeUserDetailsService.class); // Logger instance

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an {@code AcmeUserDetailsService} with the specified {@link UserRepository} and {@link PasswordEncoder}.
     *
     * @param userRepository the repository to access user data
     * @param passwordEncoder the encoder to hash passwords
     */
    public AcmeUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads a user's details by their email, as required by Spring Security's authentication mechanism.
     * Converts the {@link AcmePlexUser} into a {@link UserPrincipal}.
     * Caches the user details after retrieval.
     *
     * @param email the email identifying the user whose data is required
     * @return a {@link UserPrincipal} containing user details
     * @throws UsernameNotFoundException if no user is found with the specified email
     */
    @Override
    @Cacheable(value = "userProfileCache", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            AcmePlexUser acmePlexUser = findUserByEmail(email);
            return createUserDetails(acmePlexUser);
        } catch (Exception e) {
            logger.error("Error occurred while loading user details for email: {}", email, e);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    /**
     * Retrieves a user by their email from the database.
     *
     * @param email the email identifying the user
     * @return the {@link AcmePlexUser} entity with the specified email
     * @throws UsernameNotFoundException if no user is found with the specified email
     */
    private AcmePlexUser findUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        } catch (Exception e) {
            logger.error("Error occurred while retrieving user by email: {}", email, e);
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    /**
     * Creates a {@link UserPrincipal} from an {@link AcmePlexUser} entity.
     *
     * @param acmePlexUser the user entity to be converted into a {@link UserPrincipal}
     * @return a {@link UserPrincipal} containing the user's id, email, password, first name, and last name
     */
    private UserDetails createUserDetails(AcmePlexUser acmePlexUser) {
        return new UserPrincipal(
                acmePlexUser.getId(),
                acmePlexUser.getEmail(),
                acmePlexUser.getPassword(),
                acmePlexUser.getFirstName(),
                acmePlexUser.getLastName()
        );
    }

    /**
     * Registers a new user if the email is not already in use.
     * After the user is registered, the user's details are cached.
     *
     * @param registerRequest the request containing new user details
     * @return the newly registered user
     * @throws IllegalArgumentException if the email is already in use
     */
    @CachePut(value = "userProfileCache", key = "#registerRequest.getEmail()")
    public AcmePlexUser registerUser(RegisterRequest registerRequest) {
        try {
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already in use: " + registerRequest.getEmail());
            }

            AcmePlexUser newUser = new AcmePlexUser(
                    registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName()
            );

            logger.info("Registering new user with email: {}", registerRequest.getEmail());
            return userRepository.save(newUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed for email: {} - {}", registerRequest.getEmail(), e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        } catch (Exception e) {
            logger.error("Unexpected error occurred while registering user: {}", registerRequest.getEmail(), e);
            throw new RuntimeException("Unexpected error occurred during user registration", e);
        }
    }

    /**
     * Evicts the user's details from the cache when they are updated.
     * This ensures that the cache is updated when user details are modified.
     *
     * @param email the email of the user whose details need to be evicted from cache
     */
    @CacheEvict(value = "userProfileCache", key = "#email")
    public void evictUserFromCache(String email) {
        try {
            logger.info("Evicting user from cache with email: {}", email);
            // This method will evict the user's details from cache, forcing a fresh lookup next time
        } catch (Exception e) {
            logger.error("Error occurred while evicting user from cache: {}", email, e);
        }
    }

    /**
     * Retrieves a user by their email from the database.
     *
     * @param email the email identifying the user
     * @return an Optional containing the AcmePlexUser if found, or empty if not found
     */
    public Optional<AcmePlexUser> getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving user by email: {}", email, e);
            return Optional.empty(); // Return an empty Optional if an exception occurs
        }
    }

    /**
     * Updates the profile information of an existing user.
     *
     * @param email                 the email of the user to update
     * @param updateProfileRequest  the request containing updated profile information
     * @throws IllegalArgumentException if the user is not found
     */
    public void updateUserProfile(String email, UpdateProfileRequest updateProfileRequest) {
        try {
            // Fetch the user by email
            AcmePlexUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

            // Update fields only if they are not null or empty in the request
            if (updateProfileRequest.getFirstName() != null && !updateProfileRequest.getFirstName().isBlank()) {
                user.setFirstName(updateProfileRequest.getFirstName());
            }
            if (updateProfileRequest.getLastName() != null && !updateProfileRequest.getLastName().isBlank()) {
                user.setLastName(updateProfileRequest.getLastName());
            }
            if (updateProfileRequest.getEmail() != null && !updateProfileRequest.getEmail().isBlank()) {
                // Check for email uniqueness
                if (!user.getEmail().equals(updateProfileRequest.getEmail())
                        && userRepository.findByEmail(updateProfileRequest.getEmail()).isPresent()) {
                    throw new IllegalArgumentException("Email is already in use: " + updateProfileRequest.getEmail());
                }
                user.setEmail(updateProfileRequest.getEmail());
            }
            if (updateProfileRequest.getPassword() != null && !updateProfileRequest.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));
            }

            // Save the updated user to the repository
            userRepository.save(user);

            // Log the update operation
            logger.info("User profile updated successfully for email: {}", email);
        } catch (IllegalArgumentException e) {
            logger.warn("Error updating user profile for email {}: {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating user profile for email {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while updating user profile", e);
        }
    }

}
