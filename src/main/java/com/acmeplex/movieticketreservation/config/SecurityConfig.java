package com.acmeplex.movieticketreservation.config;

import com.acmeplex.movieticketreservation.services.AcmeUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AcmeUserDetailsService userDetailsService;

    public SecurityConfig(AcmeUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain, defining public and protected endpoints.
     *
     * @param http the HttpSecurity instance
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/auth/login", "/auth/register").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/auth/login")
                            .permitAll()
                    );

            logger.info("Security filter chain configured successfully.");
            return http.build();
        } catch (Exception e) {
            logger.error("Error configuring security filter chain", e);
            throw e; // Re-throw to be handled by the Spring context
        }
    }

    /**
     * Provides a password encoder for encoding and validating passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            logger.info("PasswordEncoder created successfully.");
            return encoder;
        } catch (Exception e) {
            logger.error("Error creating PasswordEncoder", e);
            throw e; // Re-throw to be handled by the Spring context
        }
    }

    /**
     * Configures the AuthenticationManager with the custom UserDetailsService and PasswordEncoder.
     *
     * @param http the HttpSecurity instance used to get the AuthenticationManagerBuilder
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        try {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
            AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
            logger.info("AuthenticationManager created successfully.");
            return authenticationManager;
        } catch (Exception e) {
            logger.error("Error creating AuthenticationManager", e);
            throw e; // Re-throw to be handled by the Spring context
        }
    }
}
