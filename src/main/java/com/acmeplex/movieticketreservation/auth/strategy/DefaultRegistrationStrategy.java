package com.acmeplex.movieticketreservation.auth.strategy;

import com.acmeplex.movieticketreservation.config.constants.Constants;
import com.acmeplex.movieticketreservation.dto.request.CreateBillingAddressRequest;
import com.acmeplex.movieticketreservation.dto.request.CreatePaymentTypeRequest;
import com.acmeplex.movieticketreservation.dto.request.RegisterRequest;
import com.acmeplex.movieticketreservation.dto.response.ApiResponse;
import com.acmeplex.movieticketreservation.facade.RegistrationFacade;
import com.acmeplex.movieticketreservation.model.AcmePlexUser;
import com.acmeplex.movieticketreservation.security.AuthTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Strategy for handling user registration requests.
 *
 * <p>This class implements the {@link AuthStrategy} interface to define a specific
 * registration workflow. It uses the {@link RegistrationFacade} to orchestrate
 * the user, billing address, and payment type registration steps.
 *
 * <p>The class is annotated with {@link Component} to allow Spring to manage
 * its lifecycle and inject it as a dependency where needed.
 */
@Component
public class DefaultRegistrationStrategy implements AuthStrategy<Object[]> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRegistrationStrategy.class);

    private final RegistrationFacade registrationFacade;
    private final AuthTokenUtil authTokenUtil;

    /**
     * Constructs a new instance of {@code RegisterStrategy}.
     *
     * @param registrationFacade the facade responsible for handling the registration process
     * @param authTokenUtil      the utility for generating JWT tokens
     */
    public DefaultRegistrationStrategy(RegistrationFacade registrationFacade, AuthTokenUtil authTokenUtil) {
        this.registrationFacade = registrationFacade;
        this.authTokenUtil = authTokenUtil;
    }

    /**
     * Executes the registration workflow.
     *
     * <p>This method processes the user registration by orchestrating the creation
     * of the user's account, billing address, and payment type using the provided
     * request objects. It also generates a JWT token upon successful registration.
     *
     * @param requests an array of request objects:
     *                 <ul>
     *                     <li>requests[0] - a {@link RegisterRequest} containing user details</li>
     *                     <li>requests[1] - a {@link CreateBillingAddressRequest} for the user's billing address</li>
     *                     <li>requests[2] - a {@link CreatePaymentTypeRequest} for the user's payment type</li>
     *                 </ul>
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a success
     * message and JWT token if registration is successful, or an error message if it fails
     */
    @Override
    public ResponseEntity<ApiResponse<String>> execute(Object[] requests) {
        RegisterRequest registerRequest = (RegisterRequest) requests[0];
        CreateBillingAddressRequest billingAddressRequest = (CreateBillingAddressRequest) requests[1];
        CreatePaymentTypeRequest paymentTypeRequest = (CreatePaymentTypeRequest) requests[2];

        try {
            logger.info("Registration attempt for email: {}", registerRequest.getEmail());

            // Register user
            AcmePlexUser newUser = registrationFacade.registerUser(registerRequest, billingAddressRequest, paymentTypeRequest);

            // Generate JWT token
            String token = authTokenUtil.generateToken(newUser.getEmail());
            logger.info("User {} registered successfully.", registerRequest.getEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, Constants.Success.USER_REGISTERED, token));
        } catch (IllegalArgumentException ex) {
            logger.warn("Registration failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error during registration for email: {}", registerRequest.getEmail(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, Constants.Errors.INTERNAL_SERVER_ERROR));
        }
    }
}
