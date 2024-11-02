// Package declaration: Defines that this class belongs to the controllers package.
package controllers;

// Import statements for SLF4J logging and Spring web annotations.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController is a Spring annotation that marks this class as a RESTful web controller.
 * It combines @Controller and @ResponseBody, so every method returns data directly
 * in JSON or text format, rather than rendering a view.
 *
 * @RequestMapping("/api") sets the base URL for all endpoints in this controller.
 * Every request mapped within this controller will start with "/api".
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * Logger instance for logging messages. This logger is created using LoggerFactory
     * and is set to log messages specific to this class, HelloController.
     *
     * Purpose:
     * - Logs messages to help monitor activity, debug, and understand application flow.
     */
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    /**
     * The helloWorld() method handles GET requests at "/api/hello".
     *
     * @GetMapping("/hello") maps this method to handle GET requests to "/api/hello".
     * When accessed, it logs a message and returns a simple "Hello, World!" string.
     *
     * @return A plain text response "Hello, World!".
     */
    @GetMapping("/hello")
    public String helloWorld() {
        logger.info("Received request for Hello World endpoint");
        return "Hello, World!";
    }
}
