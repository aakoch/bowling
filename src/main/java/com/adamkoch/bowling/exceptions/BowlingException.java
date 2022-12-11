package com.adamkoch.bowling.exceptions;

/**
 * Application base RuntimeException. It might be a little overkill for this app.
 */
public class BowlingException extends RuntimeException {

  public BowlingException(String message) {
    super(message);
  }

}
