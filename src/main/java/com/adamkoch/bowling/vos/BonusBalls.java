package com.adamkoch.bowling.vos;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the bonus balls for spares and strikes in the tenth frame.
 */
@EqualsAndHashCode
@ToString
public class BonusBalls {

  private final Ball ballOne;

  // Currently no need for a getter on ballTwo
  private final Ball ballTwo;

  // TODO: add validation so only valid values are passed in
  // TODO: create constructor for only 1 bonus ball
  public BonusBalls(int numOfPinsOnBallOne, int numOfPinsOnBallTwo) {
    ballOne = new Ball(numOfPinsOnBallOne);
    ballTwo = new Ball(numOfPinsOnBallTwo);
    ballOne.setNextBall(ballTwo);
  }

  /**
   * Get the first of the bonus balls. This can be null if no bonus balls were awarded.
   *
   * @return the first ball or null if no balls were awarded
   */
  public @Nullable Ball getFirstBall() {
    return ballOne;
  }

}