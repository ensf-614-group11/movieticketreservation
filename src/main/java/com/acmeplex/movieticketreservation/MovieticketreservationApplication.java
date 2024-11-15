package com.acmeplex.movieticketreservation;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The entry point of the Spring Boot application.
 * Initializes the database with sample users and starts the application.
 */
@SpringBootApplication(scanBasePackages = "com.acmeplex.movieticketreservation")
public class MovieticketreservationApplication {

	// Logger instance for the application class
	private static final Logger logger = LoggerFactory.getLogger(MovieticketreservationApplication.class);

	public static void main(String[] args) {
		// Run the Spring Boot application
		SpringApplication.run(MovieticketreservationApplication.class, args);
	}

	/**
	 * Initializes the database with a couple of sample users.
	 * This will be run once during the application startup.
	 *
	 * @param userRepository the repository to save users
	 * @param passwordEncoder the encoder for encrypting passwords
	 * @return a CommandLineRunner that runs the database initialization
	 */
	@Bean
	public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			try {
				// Adding sample users with encrypted passwords
				userRepository.save(new AcmePlexUser("admin@example.com", passwordEncoder.encode("1111"), "Admin", "User"));
				userRepository.save(new AcmePlexUser("user@example.com", passwordEncoder.encode("1111"), "Regular", "User"));

				// Log success message after initialization
				logger.info("Sample users have been added to the database.");

			} catch (Exception e) {
				// Log any exceptions that occur during the database initialization
				logger.error("Error occurred while initializing the database: {}", e.getMessage(), e);
			}
		};
	}
}
