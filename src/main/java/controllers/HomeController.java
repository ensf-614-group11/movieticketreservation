// Package declaration: Specifies that this class belongs to the controllers package.
package controllers;

// Import statements for necessary Spring MVC annotations.
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Controller is a Spring annotation used to mark this class as a web controller.
 * This annotation allows Spring to recognize this class as a handler for web requests,
 * making it a part of the application's MVC (Model-View-Controller) framework.
 */
@Controller
public class HomeController {

    /**
     * The home() method is mapped to handle HTTP GET requests for the root URL ("/").
     *
     * @GetMapping("/") maps this method to the root path of the application, so that when a
     * user accesses `http://localhost:8000/`, this method will be triggered.
     *
     * @return "index" - The method returns the name of the view (index.html) that should be rendered.
     * Spring automatically looks for this file in the `src/main/resources/templates` folder,
     * as per Thymeleaf (the default template engine for Spring Boot).
     *
     * Purpose:
     * - This method defines the default landing page of the application, serving the `index.html` file.
     */
    @GetMapping("/")
    public String home() {
        return "index"; // This loads index.html from the templates folder
    }
}
