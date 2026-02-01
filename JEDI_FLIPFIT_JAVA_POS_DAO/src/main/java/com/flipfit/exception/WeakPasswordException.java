package com.flipfit.exception;

/**
 * Exception thrown when a password does not meet strength requirements (length or complexity).
 *
 * @author Ananya
 * @ClassName "WeakPasswordException"
 */
public class WeakPasswordException extends Exception {

    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message describing the validation failure
     */
    public WeakPasswordException(String message) {
        super(message);
    }
}
