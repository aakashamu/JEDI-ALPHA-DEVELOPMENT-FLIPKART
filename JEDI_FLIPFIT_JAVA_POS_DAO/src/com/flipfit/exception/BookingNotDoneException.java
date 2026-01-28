package com.flipfit.exception;
/**
 * The Class BookingNotDoneException.
 *
 * @author Ananya
 * @ClassName  "BookingNotDoneException"
 */
public class BookingNotDoneException extends Exception {
  /**
   * Booking Not Done Exception.
   *
   * @param message the message
   * @return the public
   */
    public BookingNotDoneException(String message) {
        super(message);
    }
}
