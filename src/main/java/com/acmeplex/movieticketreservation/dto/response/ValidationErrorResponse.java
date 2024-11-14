package com.acmeplex.movieticketreservation.dto.response;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a response containing validation errors for a request.
 * This DTO provides a list of field-specific errors, each detailing which field caused the validation issue
 * and the associated error message.
 */
public class ValidationErrorResponse {

    private static final Logger logger = LoggerFactory.getLogger(ValidationErrorResponse.class);  // Logger for this class

    private List<FieldError> errors;

    /**
     * Constructs a {@code ValidationErrorResponse} with a list of field errors.
     *
     * @param errors the list of field errors in the validation response
     */
    public ValidationErrorResponse(List<FieldError> errors) {
        this.errors = errors;
        logValidationErrors();
    }

    /**
     * Returns the list of field errors.
     *
     * @return the list of field errors in this validation response
     */
    public List<FieldError> getErrors() {
        return errors;
    }

    /**
     * Sets the list of field errors.
     *
     * @param errors the list of field errors to set
     */
    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
        logValidationErrors();  // Log whenever errors are set
    }

    /**
     * Logs the validation errors to assist with debugging.
     */
    private void logValidationErrors() {
        if (errors != null && !errors.isEmpty()) {
            logger.error("Validation errors occurred:");
            for (FieldError error : errors) {
                logger.error("Field: {}, Message: {}", error.getField(), error.getMessage());
            }
        }
    }

    /**
     * Represents a specific field's validation error, including the field name and error message.
     */
    public static class FieldError {
        private String field;
        private String message;

        /**
         * Constructs a {@code FieldError} for a specific field with an error message.
         *
         * @param field   the name of the field with the validation error
         * @param message the validation error message associated with the field
         */
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        /**
         * Returns the name of the field that caused the validation error.
         *
         * @return the name of the field with the validation error
         */
        public String getField() {
            return field;
        }

        /**
         * Sets the name of the field that caused the validation error.
         *
         * @param field the name of the field to set
         */
        public void setField(String field) {
            this.field = field;
        }

        /**
         * Returns the error message associated with the validation error for the field.
         *
         * @return the validation error message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the error message associated with the validation error for the field.
         *
         * @param message the error message to set
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
