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

	public static void main(String[] args) {
		// Run the Spring Boot application
		SpringApplication.run(MovieticketreservationApplication.class, args);
	}
}
