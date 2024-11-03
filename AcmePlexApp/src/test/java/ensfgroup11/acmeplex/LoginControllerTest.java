package ensfgroup11.acmeplex;

import ensfgroup11.acmeplex.controllers.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// @WebMvcTest annotation is used for testing Spring MVC controllers.
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc is used to simulate HTTP requests and assert responses.

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(view().name("login")); // Expect to render the login view
    }

    @Test
    @WithMockUser(username = "user", password = "password") // Simulate an authenticated user
    public void testAuthenticatedUserCannotAccessLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection()); // Expect a redirection
    }
}
