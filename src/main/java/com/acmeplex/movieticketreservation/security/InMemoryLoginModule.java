package com.acmeplex.movieticketreservation.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * InMemoryLoginModule is a simple JAAS (Java Authentication and Authorization Service) LoginModule implementation.
 * It performs in-memory authentication by comparing the provided username and password with predefined values.
 */
public class InMemoryLoginModule implements LoginModule {

    // Predefined in-memory credentials
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";

    private Subject subject; // JAAS Subject representing the authenticated user
    private CallbackHandler callbackHandler; // Handles authentication callbacks for user input
    private Map<String, ?> sharedState; // Shared state between login modules (optional)
    private Map<String, ?> options; // Configuration options (optional)

    private boolean loginSucceeded = false; // Flag to track if login was successful
    private Principal userPrincipal; // Principal object representing the authenticated user

    /**
     * Initializes the login module with the provided Subject, CallbackHandler, shared state, and options.
     *
     * @param subject          the Subject to authenticate.
     * @param callbackHandler  the CallbackHandler for user input.
     * @param sharedState      shared state between login modules.
     * @param options          module-specific options.
     */
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }

    /**
     * Attempts to log in by prompting for a username and password.
     * Compares provided credentials with predefined values.
     *
     * @return true if login is successful; false otherwise.
     * @throws LoginException if login fails due to an error in handling the callbacks.
     */
    @Override
    public boolean login() throws LoginException {
        // Create callbacks to prompt for username and password
        NameCallback nameCallback = new NameCallback("username: ");
        PasswordCallback passwordCallback = new PasswordCallback("password: ", false);

        try {
            // Use the callback handler to prompt for and capture user input
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            // Check if the provided credentials match the predefined values
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                loginSucceeded = true;
            }
        } catch (IOException | UnsupportedCallbackException e) {
            // Handle exceptions related to the callback process
            throw new LoginException("Error handling authentication callbacks: " + e.getMessage());
        }

        return loginSucceeded;
    }

    /**
     * Commits the authentication if login was successful, adding the Principal to the Subject.
     *
     * @return true if commit was successful; false otherwise.
     * @throws LoginException if commit fails.
     */
    @Override
    public boolean commit() throws LoginException {
        if (!loginSucceeded) {
            return false;
        }
        // Create a Principal for the authenticated user and add it to the Subject's principals
        userPrincipal = new UserPrincipal(USERNAME);
        subject.getPrincipals().add(userPrincipal);
        return true;
    }

    /**
     * Aborts the login process if it fails or is canceled, removing any previously added principals.
     *
     * @return true if the abort was successful; false otherwise.
     * @throws LoginException if abort fails.
     */
    @Override
    public boolean abort() throws LoginException {
        if (!loginSucceeded) {
            return false;
        }

        // Reset login success flag and remove the Principal from the Subject
        loginSucceeded = false;
        if (userPrincipal != null) {
            subject.getPrincipals().remove(userPrincipal);
            userPrincipal = null;
        }
        return true;
    }

    /**
     * Logs the user out by removing the Principal from the Subject and resetting the login state.
     *
     * @return true if logout was successful; false otherwise.
     * @throws LoginException if logout fails.
     */
    @Override
    public boolean logout() throws LoginException {
        // Remove the Principal from the Subject if present
        if (userPrincipal != null) {
            subject.getPrincipals().remove(userPrincipal);
            userPrincipal = null;
        }

        // Reset login success flag
        loginSucceeded = false;
        return true;
    }
}
