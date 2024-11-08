package com.acmeplex.movieticketreservation;

import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URL;

@SpringBootApplication
public class MovieticketreservationApplication {

	public static void main(String[] args) {
		// Set the path to the JAAS configuration file in the resources directory
		URL jaasConfigUrl = MovieticketreservationApplication.class.getClassLoader().getResource("jaas.config");
		URL policyUrl = MovieticketreservationApplication.class.getClassLoader().getResource("jaas/app.policy");

		if (jaasConfigUrl != null) {
			System.setProperty("java.security.auth.login.config", jaasConfigUrl.toString());
		}

		if (policyUrl != null) {
			System.setProperty("java.security.policy", policyUrl.toString());
		}


		SpringApplication.run(MovieticketreservationApplication.class, args);

	}

	@Bean
	public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Adding a user with username 'admin' and password '1111'
			userRepository.save(new AcmePlexUser("admin", passwordEncoder.encode("1111"), "ADMIN"));
			userRepository.save(new AcmePlexUser("user", passwordEncoder.encode("password"), "USER")); // existing user
		};
	}

}
