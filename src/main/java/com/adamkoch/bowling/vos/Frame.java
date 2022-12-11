package com.adamkoch.bowling.vos;

/**
 * Frame represents the 1 or 2 ball turn in bowling.
 */
public interface Frame {

  /**
   * Get the total number of pins one or both balls knocked down in this frame.
   * <p>
   * Note: Because this method doesn't simply return a value but can do calculations, I don't prefix it with "get".
   *
   * @return the number of pins that both bolls in total knocked down
   */
  int calculateTotalPinsKnockedDown();

  /**
   * Get the score of this frame. Used when tallying up the score at the end of the game.
   * <p>
   * Note: Because this method doesn't simply return a value but can do calculations, I don't prefix it with "get".
   *
   * @return the number of points from this frame using future frames as needed
   */
  int calculateScore();

  /**
   * Get the first ball in this frame. Used to link with previous frames for scoring.
   *
   * @return first or only ball in the frame
   */
  Ball getFirstBall();

  /**
   * Set the next ball after this frame. Used in scoring.
   *
   * @param nextBall the next ball bowled
   */
  void setNextBall(Ball nextBall);

}
