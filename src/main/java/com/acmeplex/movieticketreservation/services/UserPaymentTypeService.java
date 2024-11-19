package com.acmeplex.movieticketreservation.services;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.model.Address;
import com.acmeplex.movieticketreservation.model.PaymentType;
import com.acmeplex.movieticketreservation.repository.AddressRepository;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPaymentTypeService {

    private final UserRepository userRepository;

    public UserPaymentTypeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void savePaymentType(Long userId, PaymentType paymentType) {
        AcmePlexUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setSavedPaymentType(paymentType);
        userRepository.save(user);
    }

    @Transactional
    public void removePaymentType(Long userId) {
        AcmePlexUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getSavedPaymentType() != null) {
            user.setSavedPaymentType(null);
            userRepository.save(user);
        }
    }
}
