package com.adamkoch.bowling.vos;

import lombok.EqualsAndHashCode;

/**
 * A "one ball" frame is a strike. It can also be a parent class for a "two ball" frame, so we can reuse the first ball class variable.
 */
@EqualsAndHashCode
public class OneBallFrame implements Frame {

  protected final Ball firstBall;
  protected final int frameNumber;

  public OneBallFrame(int frameNumber) {
    this(10, frameNumber);
  }

  protected OneBallFrame(int pinsKnockedDown, int frameNumber) {
    this.frameNumber = frameNumber;
    firstBall = new Ball(pinsKnockedDown);
  }

  @Override
  public Ball getFirstBall() {
    return firstBall;
  }

  @Override
  public void setNextBall(Ball nextBall) {
    firstBall.setNextBall(nextBall);
  }

  @Override
  public int calculateTotalPinsKnockedDown() {
    return 10;
  }

  @Override
  public int calculateScore() {
    // This is a bit of a gamble without null checks. In a "bullet-proof" app I would return Optional so that no
    // NPE could be thrown. But in this app, the getNextBall() is only called on the frames and having the bonus
    // balls at the end (should) mean we always have a next and a "next-next" ball.
    return 10 + firstBall.getNextBall().getPinsKnockedDown() + firstBall.getNextBall().getNextBall().getPinsKnockedDown();
  }

  @Override
  public String toString() {
    return "OneBallFrame{" +
        "firstBall=" + firstBall +
        ", frameNumber=" + frameNumber +
        '}';
  }

}