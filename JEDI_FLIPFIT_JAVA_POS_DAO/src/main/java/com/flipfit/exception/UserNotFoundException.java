package com.flipfit.exception;
/**
 * The Class UserNotFoundException.
 *
 * @author Ananya
 * @ClassName  "UserNotFoundException"
 */
public class UserNotFoundException extends Exception {
  /**
   * User Not Found Exception.
   *
   * @param message the message
   * @return the public
   */
    public UserNotFoundException(String message) {
        super(message);
    }
}
