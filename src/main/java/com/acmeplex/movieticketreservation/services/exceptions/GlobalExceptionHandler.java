package com.acmeplex.movieticketreservation.services.exceptions;

import com.acmeplex.movieticketreservation.config.constants.Constants.Errors;
import com.acmeplex.movieticketreservation.dto.response.ErrorResponse;
import com.acmeplex.movieticketreservation.dto.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for handling application-specific and generic exceptions.
 * Produces structured responses using predefined response classes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UserNotFoundException and returns a 404 response with an ErrorResponse.
     *
     * @param ex      the UserNotFoundException thrown
     * @param request the current WebRequest
     * @return a ResponseEntity with a structured ErrorResponse
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                Errors.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles validation exceptions (e.g., @Valid or @Validated annotations) and returns a 400 response
     * with a ValidationErrorResponse.
     *
     * @param ex the MethodArgumentNotValidException thrown during validation
     * @return a ResponseEntity with a structured ValidationErrorResponse
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ValidationErrorResponse> handleValidationException(Exception ex) {
        List<ValidationErrorResponse.FieldError> fieldErrors = ((BindException) ex).getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorResponse);
    }

    /**
     * Handles TicketNotFoundException and returns a 404 response with an ErrorResponse.
     *
     * @param ex      the TicketNotFoundException thrown
     * @param request the current WebRequest
     * @return a ResponseEntity with a structured ErrorResponse
     */
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                Errors.TICKET_NOT_FOUND,  // You can define a constant for this error if needed
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    /**
     * Handles InvalidRequestException and returns a 400 response with an ErrorResponse.
     *
     * @param ex      the InvalidRequestException thrown
     * @param request the current WebRequest
     * @return a ResponseEntity with a structured ErrorResponse
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                Errors.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles all other exceptions and returns a 500 response with a generic ErrorResponse.
     *
     * @param ex      the Exception thrown
     * @param request the current WebRequest
     * @return a ResponseEntity with a structured ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Errors.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
