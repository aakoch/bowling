package com.adamkoch.bowling.vos;

import lombok.*;

/**
 * A frame with 2 balls. This just means that a strike didn't happen on the first ball.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TwoBallFrame extends OneBallFrame {

  @Getter
  private final Ball secondBall;

  // TODO: have 3 ints here is really bothering me. I should make this private and instead have a builder so I can specify
  //  the fields based on appropriately named methods. I should be creating TwoBallFrames like so:
  //  TwoBallFrame frame = new TwoBallFrame.Builder().ballOnePins(1).ballTwoPins(2).frame(1).build();
  //  This would help eliminate mistakes of mixing the order and, for example, thinking the frame number was the first
  //  parameter.
  public TwoBallFrame(int ballOnePins, int ballTwoPins, int frameNumber) {
    super(ballOnePins, frameNumber);
    secondBall = new Ball(ballTwoPins);
    firstBall.setNextBall(secondBall);
  }

  @Override
  public int calculateTotalPinsKnockedDown() {
    return firstBall.getPinsKnockedDown() + secondBall.getPinsKnockedDown();
  }

  @Override
  public void setNextBall(Ball nextBall) {
    secondBall.setNextBall(nextBall);
  }

  @Override
  public int calculateScore() {
    // These are comments for reviewers of the code on some design choices by me:
    //  I don't like multiple returns in a method. It's fine for small methods, like this one, but methods have a way
    //  of growing and it gets harder later to switch back. Multiple returns makes it harder for refactoring because pulling out
    //  new methods can't be accomplished if the existing block contains a return statement.
    //  Secondly, why I'm using "final" here: "final" is a way for me to define a variable that will be assigned based branching
    //  on some logic. By having final it is preventing me from assigning the variable in one branch in making a mistake
    //  trying to re-assign it later. It's just a quick check for me.
    final int totalPins;

    if (calculateTotalPinsKnockedDown() == 10) {
      totalPins = 10 + secondBall.getNextBall().getPinsKnockedDown();
    } else {
      totalPins = calculateTotalPinsKnockedDown();
    }

    return totalPins;
  }

}