// Package declaration: This line defines the package in which the class resides.
package ensfgroup11.acmeplex;

// Import statements for required Spring Boot and configuration classes.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @SpringBootApplication is a convenience annotation that combines:
 * - @Configuration: Marks this class as a source of bean definitions.
 * - @EnableAutoConfiguration: Allows Spring Boot to automatically configure beans based on dependencies.
 * - @ComponentScan: Scans for Spring components (e.g., @Controller, @Service) within the specified packages.
 *
 * Here, the `exclude` attribute is set to exclude DataSourceAutoConfiguration.class, which prevents
 * automatic configuration of a database connection. This is useful when using in-memory databases or
 * when database setup is deferred.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan({"ensfgroup11.acmeplex", "controllers"}) // Specifies additional packages to scan for components
public class AcmeplexApplication {

	/**
	 * The main method is the entry point of the Spring Boot application.
	 * SpringApplication.run() launches the application by creating a Spring ApplicationContext,
	 * starting the embedded Tomcat server, and initiating the component scanning defined above.
	 *
	 * @param args command-line arguments passed to the application (usually empty for Spring Boot apps).
	 */
	public static void main(String[] args) {
		SpringApplication.run(AcmeplexApplication.class, args);
	}
}
