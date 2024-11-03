// Package declaration: Specifies that this class belongs to the controllers package.
package ensfgroup11.acmeplex.controllers;

// Import statements for necessary Spring MVC annotations.
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @Controller is a Spring annotation used to mark this class as a web controller.
 * This annotation allows Spring to recognize this class as a handler for web requests,
 * making it a part of the application's MVC (Model-View-Controller) framework.
 */
@Controller
public class HomeController {

    /**
     * The redirectToLogin() method is mapped to handle HTTP GET requests for the root URL ("/").
     *
     * @GetMapping("/") maps this method to the root path of the application, so that when a
     * user accesses `http://localhost:8000/`, this method will be triggered.
     *
     * @return RedirectView - The method returns a redirect view to the login page.
     *
     * Purpose:
     * - This method defines the behavior of the application for unauthenticated users,
     *   sending them to the login page.
     */
    @GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login"); // Redirect to the login page
    }

    /**
     * The home() method is mapped to handle HTTP GET requests for the home page.
     *
     * @GetMapping("/home") maps this method to the home path of the application.
     *
     * @return String - The name of the view (home.html) that should be rendered.
     *
     * Purpose:
     * - This method serves as the landing page for users after a successful login.
     */
    @GetMapping("/home")
    public String home() {
        return "home"; // Return the home view, which corresponds to home.html
    }
}
