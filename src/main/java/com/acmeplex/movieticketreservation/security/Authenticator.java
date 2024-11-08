package com.acmeplex.movieticketreservation.security;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class Authenticator {

    public static Subject authenticate() {
        try {
            // Initialize LoginContext with a CallbackHandler (e.g., ConsoleCallbackHandler)
            LoginContext loginContext = new LoginContext("Movieticketreservation", new ConsoleCallbackHandler());

            // Trigger the login process
            loginContext.login();

            // Retrieve the authenticated Subject after successful login
            return loginContext.getSubject();
        } catch (LoginException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return null;
        }
    }
}
