package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.repository.AddressRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBillingAddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserBillingAddressService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void saveBillingAddress(Long userId, Address address) {
        AcmePlexUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Address savedAddress = addressRepository.save(address);
        user.setSavedBillingAddress(savedAddress);
        userRepository.save(user);
    }

    @Transactional
    public void removeBillingAddress(Long userId) {
        AcmePlexUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getSavedBillingAddress() != null) {
            addressRepository.delete(user.getSavedBillingAddress());
            user.setSavedBillingAddress(null);
            userRepository.save(user);
        }
    }

}
