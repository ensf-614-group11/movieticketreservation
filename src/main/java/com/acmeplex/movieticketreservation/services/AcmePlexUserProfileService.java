package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.CreateUserRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.CountryCode;
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

    public AcmePlexUserProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "userProfileCache", key = "#email")
    @Transactional(readOnly = true)
    public AcmePlexUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public AcmePlexUser registerUser(CreateUserRequest registerRequest) {
        validateRegistrationRequest(registerRequest);
        checkIfEmailExists(registerRequest.getEmail());

        CountryCode countryCode = CountryCode.fromString(registerRequest.getCountryCode());

        AcmePlexUser newUser = new AcmePlexUser(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                countryCode
        );

        logger.info("Registering new user with email: {}", registerRequest.getEmail());
        return userRepository.save(newUser);
    }

    private void validateRegistrationRequest(CreateUserRequest registerRequest) {
        if (registerRequest == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) {
            throw new IllegalArgumentException("Registration details must not be null.");
        }
        logger.debug("Validated registration request for email: {}", registerRequest.getEmail());
    }

    private void checkIfEmailExists(String email) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("Email already in use: " + email);
        });
        logger.debug("Email is available: {}", email);
    }

    @Transactional
    @CacheEvict(value = "userProfileCache", key = "#email")
    public AcmePlexUser updateUserProfile(String email, UpdateProfileRequest request) {

        AcmePlexUser user = getUserByEmail(email);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (!request.getEmail().equals(user.getEmail())) {
            checkIfEmailExists(request.getEmail());
            user.setEmail(request.getEmail());
        }

        AcmePlexUser updatedUser = userRepository.save(user);
        logger.info("User profile updated successfully for email: {}", email);
        return updatedUser;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Saves or updates the user in the database.
     *
     * @param user The user to save or update.
     * @return The saved user entity.
     */
    @Transactional
    public AcmePlexUser save(AcmePlexUser user) {
        logger.debug("Saving user with email: {}", user.getEmail());
        return userRepository.save(user);
    }
}
