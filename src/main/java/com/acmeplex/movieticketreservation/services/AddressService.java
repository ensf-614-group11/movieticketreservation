package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.dto.request.BillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.repository.AddressRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import com.acmeplex.movieticketreservation.services.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
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
     * Finds the billing address for a user by their ID.
     *
     * @param userId the user's ID
     * @return the billing Address, or null if none exists
     */
    @Transactional(readOnly = true)
    public Address findBillingAddressByUserId(Long userId) {
        // Find the user by their ID
        AcmePlexUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Retrieve the billing address
        Address billingAddress = user.getSavedBillingAddress();

        if (billingAddress == null) {
            logger.warn("No billing address found for user ID: {}", userId);
        } else {
            logger.info("Retrieved billing address for user ID: {}", userId);
        }

        return billingAddress;
    }

    /**
     * Creates a new billing address for a user.
     *
     * @param email   the user's email
     * @param request the billing address creation details
     * @return the created Address
     */
    @Transactional
    public Address createBillingAddress(String email, BillingAddressRequest request) {
        AcmePlexUser user = findUserByEmail(email);

        Address address = createAddressFromRequest(request);
        Address savedAddress = addressRepository.save(address);

        assignBillingAddressToUser(user, savedAddress);
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
    public Address updateBillingAddress(String email, @Valid BillingAddressRequest request) {
        AcmePlexUser user = findUserByEmail(email);
        Address address = getBillingAddressForUser(user);

        updateAddressFields(address, request);

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
        AcmePlexUser user = findUserByEmail(email);

        Address address = user.getSavedBillingAddress();
        if (address != null) {
            addressRepository.delete(address);
            clearBillingAddressForUser(user);
            logger.info("Deleted billing address for user: {}", email);
        } else {
            logger.warn("No billing address found for user: {}", email);
        }
    }

    private AcmePlexUser findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private Address createAddressFromRequest(BillingAddressRequest request) {
        return new Address(
                request.getStreet(),
                request.getCity(),
                request.getRegion(),
                request.getPostalCode(),
                request.getCountry()
        );
    }

    private void assignBillingAddressToUser(AcmePlexUser user, Address address) {
        user.setSavedBillingAddress(address);
        userRepository.save(user);
    }

    private Address getBillingAddressForUser(AcmePlexUser user) {
        Address address = user.getSavedBillingAddress();
        if (address == null) {
            throw new IllegalArgumentException("No billing address found for user: " + user.getEmail());
        }
        return address;
    }

    private void updateAddressFields(Address address, BillingAddressRequest request) {
        if (isNotBlank(request.getStreet())) {
            address.setStreet(request.getStreet());
        }
        if (isNotBlank(request.getCity())) {
            address.setCity(request.getCity());
        }
        if (isNotBlank(request.getRegion())) {
            address.setProvince(request.getRegion());
        }
        if (isNotBlank(request.getPostalCode())) {
            address.setPostalCode(request.getPostalCode());
        }
        if (isNotBlank(request.getCountry())) {
            address.setCountry(request.getCountry());
        }
    }

    private void clearBillingAddressForUser(AcmePlexUser user) {
        user.setSavedBillingAddress(null);
        userRepository.save(user);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}