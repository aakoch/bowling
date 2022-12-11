package com.adamkoch.bowling;

import com.adamkoch.bowling.exceptions.InvalidInputException;
import com.adamkoch.bowling.vos.Ball;
import com.adamkoch.bowling.vos.Frame;
import com.adamkoch.bowling.vos.OneBallFrame;
import com.adamkoch.bowling.vos.TwoBallFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FrameFactoryTest {

    private static final int TEST_FRAME_NUMBER = 1;

    @Test
    void testParsingStrike() {
        Frame expected = new OneBallFrame(TEST_FRAME_NUMBER);

        Frame actual = FrameFactory.from("X", TEST_FRAME_NUMBER);

        assertEquals(expected, actual);
        assertEquals(10, actual.calculateTotalPinsKnockedDown());
        assertThrows(NullPointerException.class, () -> actual.calculateScore(), "Expected to get a NPE because there is no \"next ball\" with only one frame");
    }

    @Test
    void testParsingStrike_withOnlyOneNexBall() {
        Ball nextBall = new Ball(1);

        Frame actual = FrameFactory.from("X", TEST_FRAME_NUMBER);
        actual.setNextBall(nextBall);

        assertEquals(10, actual.calculateTotalPinsKnockedDown());
        assertThrows(NullPointerException.class, () -> actual.calculateScore(), "Expected to get a NPE because there is only 1 \"next ball\"");
    }

    @Test
    void testParsingStrike_withTwoNextBalls() {
        Ball nextBall = new Ball(10);
        Ball nextNextBall = new Ball(1);

        Frame actual = FrameFactory.from("X", TEST_FRAME_NUMBER);
        nextBall.setNextBall(nextNextBall);
        actual.setNextBall(nextBall);

        assertEquals(10, actual.calculateTotalPinsKnockedDown());
        assertEquals(21, actual.calculateScore());
    }

    @Test
    void testParsingSpare() {
        Frame expected = new TwoBallFrame(5, 5, TEST_FRAME_NUMBER);

        Frame actual = FrameFactory.from("5/", TEST_FRAME_NUMBER);

        assertEquals(expected, actual);
        assertEquals(10, actual.calculateTotalPinsKnockedDown());
        assertThrows(NullPointerException.class, () -> actual.calculateScore(), "Expected to get a NPE because there is no \"next ball\" with only one frame");
    }

    @Test
    void testRegularFrame() {
        Frame expected = new TwoBallFrame(2, 1, TEST_FRAME_NUMBER);

        Frame actual = FrameFactory.from("21", TEST_FRAME_NUMBER);

        assertEquals(expected, actual);
        assertEquals(3, actual.calculateTotalPinsKnockedDown());
        assertEquals(expected.calculateScore(), actual.calculateScore());
    }

    @Test
    void testGutterBall() {
        Frame expected = new TwoBallFrame(2, 0, TEST_FRAME_NUMBER);

        Frame actual = FrameFactory.from("2-", TEST_FRAME_NUMBER);

        assertEquals(expected, actual);
        assertEquals(2, actual.calculateTotalPinsKnockedDown());
        assertEquals(expected.calculateScore(), actual.calculateScore());
    }

    @Test
    void testGutterBalls() {
        TwoBallFrame expected = new TwoBallFrame(0, 0, TEST_FRAME_NUMBER);

        Frame actual = FrameFactory.from("--", TEST_FRAME_NUMBER);

        assertEquals(expected, actual);
        assertEquals(0, actual.calculateTotalPinsKnockedDown());
        assertEquals(expected.calculateScore(), actual.calculateScore());
    }

    // Why does "Z" not throw a NumberFormatException? Because for strings of length 1 it will only check if it is
    // an "X". It doesn't try to parse it.
    @ParameterizedTest
    @ValueSource(strings = {"/", "123", "8X", "Z", "", "/9"})
    void testInvalidInputs(String input) {
        assertThrows(InvalidInputException.class, () -> FrameFactory.from(input, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"9A", "1Z"})
    void testNumberFormatExceptions(String input) {
        assertThrows(NumberFormatException.class, () -> FrameFactory.from(input, 1));
    }
}