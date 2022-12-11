package com.adamkoch.bowling;

import com.adamkoch.bowling.vos.BonusBalls;
import com.adamkoch.bowling.vos.Frame;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A game represents the 10 frames plus any bonus balls in the game of bowling. Use of this application should use
 * the Main class for command line access and as a way to pass in a string representation of a game.
 * <p>
 * See INSTURCTIONS.md
 */
@EqualsAndHashCode
public class Game {

  private static final String BONUS_BALLS_DELIMITER = "||";
  private static final String FRAME_DELIMITER = "|";

  private final List<? extends Frame> frames;
  private final BonusBalls bonusBalls;

  // originally wanted a statistics object that could hold things such as number of turkeys, but decided not to
  private int score;

  /**
   * Constructor that takes in a list of Frame objects and a BonusBalls object. If you want to convert a string to a
   * Game, see {@link Main#oneStep(String)}.
   * <p>
   * Note: a game represents a past game, meaning it was already played and you are just reconstructing it for scoring.
   *
   * @param frames     A list of Frame objects
   * @param bonusBalls Bonus balls awarded
   */
  public Game(List<? extends Frame> frames, BonusBalls bonusBalls) {
    this.frames = Collections.unmodifiableList(frames);
    this.bonusBalls = bonusBalls;

    linkBalls();
  }

  /**
   * Parses a string in a format specified in the description of this project.
   * <p>
   * Parsing should probably be delegated to another class.
   *
   * @param frameString The formatted string.
   * @return A Game object ready to be scored.
   */
  public static Game fromString(String frameString) {
    // each frame is split by a pipe ("|")
    // Let's take off that "||" at the end which messes with my split()
    String bonus = frameString.substring(frameString.indexOf(BONUS_BALLS_DELIMITER) + 2);

    // the additional square brackets are because our current frame delimiter is also used in regex as "or". This won't
    // work if the delimiter is changed to an opening square bracket for example.
    String[] frameStrings = frameString.substring(0, frameString.indexOf(BONUS_BALLS_DELIMITER))
        .split("[" + FRAME_DELIMITER + "]");

    // I don't think people use assertions in production code (or anywhere) but we might catch a bug in pre-prod this way
    assert frameStrings.length == 10;

    // I needed a "final" object for use in my lambda even though the frame number isn't necessary from a requirements
    // point of view.
    AtomicInteger frameNumber = new AtomicInteger(1);
    List<Frame> frameList = Arrays.stream(frameStrings)
        .map(singleFrameString -> FrameFactory.from(singleFrameString, frameNumber.getAndIncrement()))
        .collect(Collectors.toList());
    BonusBalls bonusBalls = FrameFactory.fromBonus(bonus);
    return new Game(frameList, bonusBalls);
  }

  /**
   * Internally link the balls so we can score later.
   * <p>
   * A more sophisticated version of this application could create a container for the frames instead of just a list of
   * them and handle linking differently.
   */
  private void linkBalls() {
    int framesSize = frames.size();
    for (int i = 0; i < framesSize - 1; i++) {
      frames.get(i).setNextBall(frames.get(i + 1).getFirstBall());
    }
    Frame tenthFrame = frames.get(framesSize - 1);
    if (bonusBalls != null) {
      tenthFrame.setNextBall(bonusBalls.getFirstBall());
    }
  }

  /**
   * Returns the score for this game. Note: a game is currently only implemented for one player. Which makes it a
   * lonely game if you ask me.
   *
   * @return the score for this game
   */
  public int calculateScore() {
    return frames.stream().mapToInt(Frame::calculateScore).sum();
  }

  @Override
  public String toString() {
    return "Game{frames=" + frames.stream().map(Object::toString).collect(Collectors.joining("\n")) +
        ", bonusBalls=" + bonusBalls + ", score=" + score + '}';
  }

}