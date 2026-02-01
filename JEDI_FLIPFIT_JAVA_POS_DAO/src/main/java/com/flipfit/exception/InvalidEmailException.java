package com.flipfit.exception;

/**
 * Exception thrown when an email value does not match the required format (e.g. missing '@' or invalid domain).
 *
 * @author Ananya
 * @ClassName "InvalidEmailException"
 */
public class InvalidEmailException extends Exception {

    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message describing the validation failure
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
