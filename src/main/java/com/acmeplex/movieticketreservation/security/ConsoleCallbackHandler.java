package com.acmeplex.movieticketreservation.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.Console;

/**
 * ConsoleCallbackHandler is an implementation of the CallbackHandler interface.
 * This class handles authentication callbacks for user input from the console.
 * It supports both username (NameCallback) and password (PasswordCallback) prompts.
 */
public class ConsoleCallbackHandler implements CallbackHandler {

    /**
     * Handles an array of Callback objects, prompting the user for input via the console.
     * For each callback, it checks if it is a NameCallback or PasswordCallback and prompts
     * the user accordingly. If the callback is not supported, an UnsupportedCallbackException is thrown.
     *
     * @param callbacks An array of Callback instances that require user input.
     * @throws UnsupportedCallbackException if a callback type is unsupported.
     */
    @Override
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        // Obtain the system console for reading user input
        Console console = System.console();

        // Iterate through each callback provided in the array
        for (Callback callback : callbacks) {
            // Handle NameCallback: prompt for username input
            if (callback instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callback;
                // Read the username input from the console and set it in the callback
                nameCallback.setName(console.readLine(nameCallback.getPrompt()));

                // Handle PasswordCallback: prompt for password input
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                // Read the password input from the console securely and set it in the callback
                passwordCallback.setPassword(console.readPassword(passwordCallback.getPrompt()));

                // Throw an exception if the callback type is not supported
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}
