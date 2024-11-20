package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.request.SaveBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.SavePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateProfileRequest;
import com.acmeplex.movieticketreservation.factory.PaymentTypeFactory;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.AddressRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcmePlexUserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexUserProfileService.class);

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentTypeFactory paymentTypeFactory;
    private final PasswordEncoder passwordEncoder;

    public AcmePlexUserProfileService(
            UserRepository userRepository,
            AddressRepository addressRepository,
            PaymentTypeFactory paymentTypeFactory,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentTypeFactory = paymentTypeFactory;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email of the user
     * @return the AcmePlexUser object
     */
    @Cacheable(value = "userProfileCache", key = "#email")
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
     */
    @Transactional
    public void updateUserProfile(String email, UpdateProfileRequest request) {
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

        userRepository.save(user);
        logger.info("User profile updated successfully for email: {}", email);
    }

    /**
     * Saves or updates the billing address for a user.
     *
     * @param email the email of the user
     * @param request the billing address details
     */
    @Transactional
    public void saveBillingAddress(String email, SaveBillingAddressRequest request) {
        AcmePlexUser user = getUserByEmail(email);

        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getProvince(),
                request.getPostalCode(),
                null // Country can be added if applicable
        );

        Address savedAddress = addressRepository.save(address);
        user.setSavedBillingAddress(savedAddress);
        userRepository.save(user);
        logger.info("Billing address saved for user: {}", email);
    }

    /**
     * Removes the billing address for a user.
     *
     * @param email the email of the user
     */
    @Transactional
    public void removeBillingAddress(String email) {
        AcmePlexUser user = getUserByEmail(email);

        if (user.getSavedBillingAddress() != null) {
            addressRepository.delete(user.getSavedBillingAddress());
            user.setSavedBillingAddress(null);
            userRepository.save(user);
        }
        logger.info("Billing address removed for user: {}", email);
    }

    /**
     * Saves or updates the payment type for a user.
     *
     * @param email the email of the user
     * @param request the payment type details
     */
    @Transactional
    public void savePaymentType(String email, SavePaymentTypeRequest request) {
        AcmePlexUser user = getUserByEmail(email);

        PaymentType paymentType = paymentTypeFactory.createPaymentType(request);
        paymentType.setAcmePlexUser(user);

        user.setSavedPaymentType(paymentType);
        userRepository.save(user);
        logger.info("Payment type saved for user: {}", email);
    }

    /**
     * Removes the payment type for a user.
     *
     * @param email the email of the user
     */
    @Transactional
    public void removePaymentType(String email) {
        AcmePlexUser user = getUserByEmail(email);

        if (user.getSavedPaymentType() != null) {
            user.setSavedPaymentType(null);
            userRepository.save(user);
        }
        logger.info("Payment type removed for user: {}", email);
    }
}
