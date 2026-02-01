package com.flipfit.exception;

/**
 * Exception thrown when card or ID details (e.g. Aadhaar, PAN) are invalid in format or length.
 *
 * @author Ananya
 * @ClassName "InvalidCardDetailsException"
 */
public class InvalidCardDetailsException extends Exception {

    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message describing the validation failure
     */
    public InvalidCardDetailsException(String message) {
        super(message);
    }
}
