package com.adamkoch.bowling.exceptions;

/**
 * An exception used when validating input or trying to parse the input. If the input is wrong in some way, this exception
 * will be thrown.
 */
public class InvalidInputException extends BowlingException {

  public InvalidInputException(String message) {
    super(message);
  }

}
