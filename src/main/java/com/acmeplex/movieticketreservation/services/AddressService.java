package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.UpdateBillingAddressRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.repository.AddressRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing address-related operations.
 */
@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new billing address for a user.
     *
     * @param email   the user's email
     * @param request the billing address creation details
     * @return the created Address
     */
    @Transactional
    public Address createBillingAddress(String email, CreateBillingAddressRequest request) {
        AcmePlexUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getProvince(),
                request.getPostalCode(),
                request.getCountry()
        );

        Address savedAddress = addressRepository.save(address);
        user.setSavedBillingAddress(savedAddress);
        userRepository.save(user);

        logger.info("Created billing address for user: {}", email);
        return savedAddress;
    }

    /**
     * Updates an existing billing address for a user.
     *
     * @param email   the user's email
     * @param request the billing address update details
     * @return the updated Address
     */
    @Transactional
    public Address updateBillingAddress(String email, UpdateBillingAddressRequest request) {
        AcmePlexUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Address address = user.getSavedBillingAddress();
        if (address == null) {
            throw new IllegalArgumentException("No billing address found for user: " + email);
        }

        if (request.getStreet() != null && !request.getStreet().isBlank()) {
            address.setStreet(request.getStreet());
        }
        if (request.getCity() != null && !request.getCity().isBlank()) {
            address.setCity(request.getCity());
        }
        if (request.getProvince() != null && !request.getProvince().isBlank()) {
            address.setProvince(request.getProvince());
        }
        if (request.getPostalCode() != null && !request.getPostalCode().isBlank()) {
            address.setPostalCode(request.getPostalCode());
        }
        if (request.getCountry() != null && !request.getCountry().isBlank()) {
            address.setCountry(request.getCountry());
        }

        Address updatedAddress = addressRepository.save(address);
        logger.info("Updated billing address for user: {}", email);
        return updatedAddress;
    }

    /**
     * Deletes the billing address for a user.
     *
     * @param email the user's email
     */
    @Transactional
    public void deleteBillingAddress(String email) {
        AcmePlexUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Address address = user.getSavedBillingAddress();
        if (address != null) {
            addressRepository.delete(address);
            user.setSavedBillingAddress(null);
            userRepository.save(user);

            logger.info("Deleted billing address for user: {}", email);
        } else {
            logger.warn("No billing address found for user: {}", email);
        }
    }
}
