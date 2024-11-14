package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
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
}
