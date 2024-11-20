package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing user profile operations such as registration, profile updates,
 * and billing address management.
 */
@Service
public class AcmePlexUserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexUserProfileService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AcmePlexUserProfileService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email of the user
     * @return the AcmePlexUser object
     */
    @Cacheable(value = "userProfileCache", key = "#email")
    @Transactional(readOnly = true)
    public AcmePlexUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    /**
     * Registers a new user.
     *
     * @param registerRequest the registration details
     * @return the newly created AcmePlexUser
     */
    @Transactional
    public AcmePlexUser registerUser(RegisterRequest registerRequest) {
        if (registerRequest == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) {
            throw new IllegalArgumentException("Registration details must not be null.");
        }

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
    }

    /**
     * Updates user profile details.
     *
     * @param email   the email of the user
     * @param request the profile update request
     * @return the updated AcmePlexUser object
     */
    @Transactional
    @CacheEvict(value = "userProfileCache", key = "#email")
    public AcmePlexUser updateUserProfile(String email, UpdateProfileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update request must not be null.");
        }

        AcmePlexUser user = getUserByEmail(email);

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already in use: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        AcmePlexUser updatedUser = userRepository.save(user);
        logger.info("User profile updated successfully for email: {}", email);
        return updatedUser;
    }
}
