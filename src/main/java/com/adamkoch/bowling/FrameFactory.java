package com.adamkoch.bowling;

import com.adamkoch.bowling.exceptions.InvalidInputException;
import com.adamkoch.bowling.vos.BonusBalls;
import com.adamkoch.bowling.vos.Frame;
import com.adamkoch.bowling.vos.OneBallFrame;
import com.adamkoch.bowling.vos.TwoBallFrame;

import java.util.Objects;

/**
 * This is not a factory as described as a design pattern. The "Abstract Factory" returns concrete implementations for
 * a family of classes. The "Factory Method" defines an abstract method for creating objects but lets the subclasses do
 * the actual instantiation.
 * <p>
 * This class is used to parse the input into a frame or the bonus balls.
 */
public final class FrameFactory {

  private static final String EXCESS_DATA_ERROR_MESSAGE = "There were more than 2 characters when trying to parse this frame. Input=";
  private static final String FIRST_BALL_SPARE_ERROR_MSG = "You can't get a spare on the first ball in a frame";

  // These could be public if they were used by other classes. But if that was the case it should be in a different class.
  private static final String STRIKE_SYMBOL = "X";
  private static final String SPARE_SYMBOL = "/";
  private static final String MISS_SYMBOL = "-";

  private FrameFactory() {
    // utility classes shouldn't be instantiated
  }

  /**
   * Create a frame from a string representation. The representation should be 1 or 2 characters with "X" meaning
   * a strike, a "/" meaning a spare a "-" meaning a miss or a number 0-9 meaning the number of pins knocked down.
   *
   * @param singleFrameString A string representation of a frame
   * @param frameNumber       Which frame in the game this particular frame is for
   * @return A frame
   * @throws NumberFormatException In cases the initial validation is bypassed and this method is called directly in a
   *                               different context, there's the possibility we try to parse something that isn't an
   *                               integer. In these cases a NumberFormatException is thrown
   * @throws InvalidInputException This is thrown in the unlikely cases our initial validation didn't catch everything
   */
  public static Frame from(String singleFrameString, int frameNumber) {
    Objects.requireNonNull(singleFrameString,
        "Received a null for a single frame. I'm not sure how this happened as the split() method would've " +
            "returned an empty string and not a null");

    if (singleFrameString.isEmpty()) {
      throw new InvalidInputException("Player has an unscored frame");
    } else if (singleFrameString.length() == 1) {
      if (singleFrameString.equals(STRIKE_SYMBOL)) {
        return new OneBallFrame(frameNumber);
      } else {
        throw new InvalidInputException(
            "Expected a frame with one character to be \"X\" but it was " + singleFrameString);
      }
    } else if (singleFrameString.length() > 2) {
      throw new InvalidInputException(EXCESS_DATA_ERROR_MESSAGE + singleFrameString);
    } else {
      String firstThrow = singleFrameString.substring(0, 1);

      // I've been waiting for a chance to use switch expressions. We're still on Java 8 at work.
      int numOfPinsOnFirstBall = switch (firstThrow) {
        case SPARE_SYMBOL -> throw new InvalidInputException(FIRST_BALL_SPARE_ERROR_MSG);
        case MISS_SYMBOL -> 0;
        // this could produce a NumberFormatException if this was called directly (bypassed my regex)
        default -> Integer.parseInt(firstThrow);
      };

      String secondThrow = singleFrameString.substring(1, 2);
      return switch (secondThrow) {
        case SPARE_SYMBOL -> new TwoBallFrame(numOfPinsOnFirstBall, 10 - numOfPinsOnFirstBall, frameNumber);
        case MISS_SYMBOL -> new TwoBallFrame(numOfPinsOnFirstBall, 0, frameNumber);
        case STRIKE_SYMBOL -> throw new InvalidInputException("A strike from the second ball in a frame isn't possible");
        // this could produce a NumberFormatException if this was called directly (bypassed my regex)
        default -> new TwoBallFrame(numOfPinsOnFirstBall, Integer.parseInt(secondThrow), frameNumber);
      };
    }
  }

  /**
   * Create an object for the 0, 1 or 2 bonus balls after the tenth frame.
   *
   * @param bonus A string representation of the number of pins knocked down
   * @return A BonusBalls object
   * @throws NumberFormatException In cases the initial validation is bypassed and this method is called directly in a
   *                               different context, there's the possibility we try to parse something that isn't an
   *                               integer. In these cases a NumberFormatException is thrown
   * @throws InvalidInputException This is thrown in the unlikely cases our initial validation didn't catch everything
   */
  public static BonusBalls fromBonus(String bonus) {
    Objects.requireNonNull(bonus, "Received a null for the bonus balls");

    if (bonus.isEmpty()) {
      // even if there is nothing after the "||" we need something in case the tenth frame was a strike
      return new BonusBalls(0, 0);
    } else if (bonus.length() == 1) {
      if (bonus.equals(STRIKE_SYMBOL)) {
        return new BonusBalls(10, 10);
      } else {
        return new BonusBalls(Integer.parseInt(bonus), 0);
      }
    } else if (bonus.length() > 2) {
      throw new InvalidInputException(EXCESS_DATA_ERROR_MESSAGE + bonus);
    } else {

      String firstThrow = bonus.substring(0, 1);
      int numOfPinsOnFirstBall = switch (firstThrow) {
        case SPARE_SYMBOL -> throw new InvalidInputException(FIRST_BALL_SPARE_ERROR_MSG);
        case MISS_SYMBOL -> 0;
        case STRIKE_SYMBOL -> 10;
        // this could produce a NumberFormatException if this was called directly (bypassed my regex)
        default -> Integer.parseInt(firstThrow);
      };

      String secondThrow = bonus.substring(1, 2);
      return switch (secondThrow) {
        case SPARE_SYMBOL -> new BonusBalls(numOfPinsOnFirstBall, 10 - numOfPinsOnFirstBall);
        case MISS_SYMBOL -> new BonusBalls(numOfPinsOnFirstBall, 0);
        case STRIKE_SYMBOL -> new BonusBalls(10, 10);
        // this could produce a NumberFormatException if this was called directly (bypassed my regex)
        default -> new BonusBalls(numOfPinsOnFirstBall, Integer.parseInt(secondThrow));
      };
    }
  }

}