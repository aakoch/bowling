package com.adamkoch.bowling.vos;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Ball represents the actual roll of the ball. This class should only be instantiated after the bowl and by passing in
 * the pins it knocked down.
 * <p>
 * This class allows for the linking to the next ball for scoring. See setNextBall()
 */
@EqualsAndHashCode
// I want getters on each field but not setters as I want the pins knocked down to be passed in the parameter and not changed.
@Getter
public class Ball {

  private final int pinsKnockedDown;

  // Purposely not including nextBall in the toString() method because it will chain all the balls and is cluttered.
  // The warning suppression is because I usually want to be notified in IntelliJ when a field is missing.
  @SuppressWarnings("FieldNotUsedInToString")
  private Ball nextBall;

  /**
   * Constructor for a ball that enforces instantiation after the bowl since it requires the number of pins the ball
   * knocked down.
   *
   * @param pinsKnockedDown number of pins the ball knocked down
   */
  public Ball(int pinsKnockedDown) {
    // no one uses assert in production, do they? Seems appropriate here though to ensure valid amount
    assert pinsKnockedDown >= 0 && pinsKnockedDown <= 10;
    this.pinsKnockedDown = pinsKnockedDown;
  }

  /**
   * Set the next ball. Used in scoring.
   *
   * @param nextBall The ball that was bowled next.
   */
  public void setNextBall(Ball nextBall) {
    this.nextBall = nextBall;
  }

  @Override
  public String toString() {
    return "Ball{" +
        "pinsKnockedDown=" + pinsKnockedDown +
        "}";
  }

}
