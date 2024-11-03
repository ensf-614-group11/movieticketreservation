package ensfgroup11.acmeplex.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public ModelAndView login(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // User is authenticated; redirect to home
            return new ModelAndView("redirect:/home");
        }
        // User is not authenticated; show login page
        return new ModelAndView("login");
    }
}
