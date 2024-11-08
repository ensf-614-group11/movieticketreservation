package com.acmeplex.movieticketreservation.service;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcmeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String ROLE_SEPARATOR = ","; // Define a constant for role separator

    public AcmeUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AcmePlexUser acmePlexUser = findUserByUsername(username);
        return createUserDetails(acmePlexUser);
    }

    private AcmePlexUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private UserDetails createUserDetails(AcmePlexUser acmePlexUser) {
        return User.builder()
                .username(acmePlexUser.getUsername())
                .password(acmePlexUser.getPassword())
                .roles(acmePlexUser.getRoles().split(ROLE_SEPARATOR)) // Use the constant for splitting roles
                .build();
    }
}
