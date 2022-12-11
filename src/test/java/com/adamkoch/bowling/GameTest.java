package com.adamkoch.bowling;

import com.adamkoch.bowling.vos.BonusBalls;
import com.adamkoch.bowling.vos.Frame;
import com.adamkoch.bowling.vos.OneBallFrame;
import com.adamkoch.bowling.vos.TwoBallFrame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests both parsing and scoring.
 */
class GameTest {

  static Object[][] gameOutcomes() {
    return new Object[][]{
        {"9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||", 90}, {"X|X|X|X|X|X|X|X|X|X||XX", 300},
        {"5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5", 150}, {"X|7/|9-|X|-8|8/|-6|X|X|X||81", 167},
        {"54|4/|7-|X|X|X|53|6/|4/|X||XX", 178}, {"14|45|6/|5/|X|01|7/|6/|X|2/||6", 133}};
  }

  static Object[][] gameObjects() {
    return new Object[][]{
        {
            "X|7/|9-|X|-8|8/|-6|X|X|X||81", new GameBuilder().strike()
            .frame(7, 3)
            .frame(9, 0)
            .strike()
            .frame(0, 8)
            .frame(8, 2)
            .frame(0, 6)
            .strikes(3)
            .bonusBalls(8, 1).build()},
        {"X|X|X|X|X|X|X|X|X|X||", new GameBuilder().strikes(10).bonusBalls(0, 0).build()},
        {"5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5", new GameBuilder().frames(10, 5, 5).bonusBalls(5, 0).build()},
        {"4-|5/|5/|5/|5/|5/|5/|5/|5/|5/||5", new GameBuilder().frame(4, 0).frames(9, 5, 5).bonusBalls(5, 0).build()}};
  }

  @ParameterizedTest
  @MethodSource("gameOutcomes")
  void testGameScores(String framesString, int expectedScore) {
    Game g = Game.fromString(framesString);
    int score = g.calculateScore();
    assertEquals(expectedScore, score);
  }

  @ParameterizedTest
  @MethodSource("gameObjects")
  void testGameObjects(String framesString, Game expectedGame) {
    Game g = Game.fromString(framesString);
    assertEquals(expectedGame, g);
  }

  // This was a test when I had validation accessible through a static, public method on my Game class. It might be worth
  // revisiting validation if this were ever truly a product.
//    @ParameterizedTest
//    @ValueSource(strings = {
//            "X|X|X|X|X|X|X|X|X|X||XX",
//            "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||",
//            "5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5",
//            "X|7/|9-|X|-8|8/|-6|X|X|X||81"
//    })
//    void isValidInput(String input) {
//        assertTrue(Game.isValidInput(new String[]{input}), "Expected input to be valid but wasn't: " + input);
//    }

  /**
   * Helper class
   */
  @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
  private static class GameBuilder {
    private int frameNumber = 1;
    private List<Frame> frames;
    private BonusBalls bonusBalls;

    private GameBuilder() {
      frames = new ArrayList<>(10);
      bonusBalls = new BonusBalls(0, 0);
    }

    GameBuilder bonusBalls(int numOfPinsKnockedDownFromFirstBall, int numOfPinsKnockedDownFromSecondBall) {
      bonusBalls = new BonusBalls(numOfPinsKnockedDownFromFirstBall, numOfPinsKnockedDownFromSecondBall);
      return this;
    }

    GameBuilder frame(int numOfPinsKnockedDownFromFirstBall, int numOfPinsKnockedDownFromSecondBall) {
      frames.add(
          new TwoBallFrame(numOfPinsKnockedDownFromFirstBall, numOfPinsKnockedDownFromSecondBall, frameNumber++));
      return this;
    }

    public GameBuilder frames(int numOfFrames, int pinsKnockedOverByBallOne, int pinsKnockedOverByBallTwo) {
      frames.addAll(IntStream.range(0, numOfFrames)
          .mapToObj((i) -> new TwoBallFrame(pinsKnockedOverByBallOne, pinsKnockedOverByBallTwo, frameNumber++))
          .toList());
      return this;
    }

    GameBuilder strike() {
      frames.add(new OneBallFrame(frameNumber++));
      return this;
    }

    GameBuilder strikes(int numOfFrames) {
      frames.addAll(IntStream.range(0, numOfFrames).mapToObj((i) -> new OneBallFrame(frameNumber++)).toList());
      return this;
    }

    Game build() {
      return new Game(frames, bonusBalls);
    }
  }

}