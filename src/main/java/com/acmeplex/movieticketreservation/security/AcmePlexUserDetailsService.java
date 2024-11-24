package com.acmeplex.movieticketreservation.security;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service that implements {@link UserDetailsService} to load user-specific data for authentication.
 * Uses {@link AcmePlexUser} as the user model and provides a {@link UserPrincipal} with the user's details.
 * Also handles user registration and caching of user data.
 */
@Service
public class AcmePlexUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AcmePlexUserDetailsService.class);

    private final UserRepository userRepository;

    public AcmePlexUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userProfileCache", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserPrincipal(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getCountryCode()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
