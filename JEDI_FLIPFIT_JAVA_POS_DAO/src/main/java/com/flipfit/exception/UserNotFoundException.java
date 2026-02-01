package com.flipfit.exception;
/**
 * The Class UserNotFoundException.
 *
 * @author Ananya
 * @ClassName  "UserNotFoundException"
 */
public class UserNotFoundException extends Exception {
    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
