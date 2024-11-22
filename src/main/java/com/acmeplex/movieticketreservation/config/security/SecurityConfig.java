package com.acmeplex.movieticketreservation.config.security;

import com.acmeplex.movieticketreservation.security.AuthTokenFilterUtil;
import com.acmeplex.movieticketreservation.security.AcmePlexUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AcmePlexUserDetailsService userDetailsService;
    private final AuthTokenFilterUtil authTokenFilterUtil;

    /**
     * Constructor to inject dependencies.
     *
     * @param userDetailsService the custom UserDetailsService implementation
     * @param authTokenFilterUtil the JWT authentication filter
     */
    @Lazy
    public SecurityConfig(AcmePlexUserDetailsService userDetailsService, AuthTokenFilterUtil authTokenFilterUtil) {
        this.userDetailsService = userDetailsService;
        this.authTokenFilterUtil = authTokenFilterUtil;
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

        // Define public and protected endpoints directly here
        List<String> publicEndpoints = List.of(
                "/api/auth/login/**",
                "/api/auth/register/**",
                "/public/**",     // Static files and public resources
                "/static/**",
                "/favicon.ico",
                "/error"
        );

        List<String> protectedEndpoints = List.of(
                "/api/user/profile/**",
                "/api/user/profile/billing-address/**",
                "/api/user/profile/payment-type/**",
                "/api/user/profile/payment-type/manage/**"
        );

        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // Your frontend origin
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Include OPTIONS
                    corsConfig.setAllowedHeaders(List.of("*")); // Allow all headers
                    corsConfig.setAllowCredentials(true); // Allow cookies/auth tokens
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests to public endpoints
                        .requestMatchers(publicEndpoints.toArray(new String[0])).permitAll()
                        // Require authentication for protected endpoints
                        .requestMatchers(protectedEndpoints.toArray(new String[0])).authenticated()
                        // Require authentication for any other requests
                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider()) // Use custom UserDetailsService
                .addFilterBefore(authTokenFilterUtil, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        logger.info("Security filter chain configured with CORS.");
        return http.build();
    }

    /**
     * Configures the password encoder for encoding and validating passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager using AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the configuration for authentication
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures a DaoAuthenticationProvider using the custom UserDetailsService and PasswordEncoder.
     *
     * @return a configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
