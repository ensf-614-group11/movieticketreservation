// Package declaration: This line specifies the package to which this class belongs.
package ensfgroup11.acmeplex;

// Import the HelloController from the controllers package.
// This is the class that we are testing in this file.
import controllers.HelloController;

// Import statements for JUnit testing framework and Spring MVC testing utilities.
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// Import static method for checking the HTTP status in response.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WebMvcTest is a specialized test annotation for Spring MVC components.
 * Here, it specifies that we are only testing the HelloController class in isolation.
 * This annotation helps Spring set up the necessary application context components
 * needed for testing HelloController without starting the entire application.
 */
@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    /**
     * The MockMvc object is a main utility provided by Spring for testing Spring MVC controllers.
     * It allows you to send simulated HTTP requests and verify the responses
     * without needing an actual server.
     * Autowiring here injects an instance of MockMvc created by Spring.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test method to verify the "/api/hello" endpoint of HelloController.
     * This test checks that a GET request to the endpoint responds with HTTP status 200 (OK),
     * indicating that the endpoint is reachable and works as expected.
     *
     * @throws Exception if an error occurs during request execution
     */
    @Test
    public void testHelloWorldEndpoint() throws Exception {
        // Perform a GET request to the /api/hello endpoint.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/hello"))
                // Verify that the response status is 200 OK.
                .andExpect(status().isOk());
    }
}
