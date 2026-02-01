package com.flipfit.exception;

/**
 * Exception thrown when a centre, owner, or booking ID is invalid (e.g. not a positive number or not numeric).
 *
 * @author Ananya
 * @ClassName "InvalidCentreIdException"
 */
public class InvalidCentreIdException extends Exception {

    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message describing the validation failure
     */
    public InvalidCentreIdException(String message) {
        super(message);
    }
}
