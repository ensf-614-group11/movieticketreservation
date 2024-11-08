package com.acmeplex.movieticketreservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The StartupLogger class is a Spring component that implements CommandLineRunner.
 * This class runs automatically after the application context is fully initialized.
 * It logs a message to the console and to the logger, confirming the server started successfully.
 * It also logs the server's local URL, showing which port the application is running on.
 */
@Component
public class StartupLogger implements CommandLineRunner {

    // Logger instance for this class, used to log messages at different levels
    private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

    // Injects the server port from application.properties, allowing us to include it in the startup message
    @Value("${server.port}")
    private String serverPort;

    /**
     * The run method is executed automatically by Spring Boot after the application context is loaded.
     * This method logs a startup message to both the console and the logger.
     *
     * @param args command-line arguments passed to the application, not used here
     */
    @Override
    public void run(String... args) {
        // Print a message to the console to confirm the server has started successfully
        System.out.println("CommandLineRunner: Server started successfully!");

        // Log messages at the INFO level to indicate successful startup and the application URL
        logger.info("Server started successfully!");
        logger.info("Application is running at: http://localhost:" + serverPort);
    }
}
