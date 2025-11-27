package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oopopps.command.AnswerCommand;
import com.oopopps.command.RemoteControl;
import com.oopopps.display.ScoreObserver;

/**
 * Integration test suite for core game logic and command pattern implementation.
 * Validates the interaction between game components including command execution,
 * score management, undo functionality, and observer pattern integration.
 * Demonstrates end-to-end gameplay scenarios with proper state management.
 */
public class GameLogicTest {

    /** Sample player, question, scoreboard and logger used for answer validation testing across all test methods */
    private Player player;
    private Question question;
    private EventLogger logger;
    private ScoreObserver scoreboard;

    /**
     * Test double implementation of EventLogger that suppresses actual file output.
     * Provides controlled testing environment while maintaining interface compliance
     * for command dependency injection.
     */
    private static class FakeEventLogger extends EventLogger {
        public FakeEventLogger() throws Exception {
            super("testlog.csv", "TEST"); 
        }

        /**
         * No-op implementation that captures method calls without side effects.
         * 
         * @param playerId identifier of the player performing the action
         * @param activity description of the game activity being logged
         * @param category question category associated with the activity
         * @param qValue point value of the question being answered
         * @param answer player's selected answer choice
         * @param result outcome of the answer attempt (CORRECT/WRONG)
         * @param scoreAfter player's score after the activity completion
         */
        @Override
        public void log(String playerId, String activity, String category,
                        String qValue, String answer, String result, int scoreAfter) {
        }
    }

    /**
     * Test fixture setup executed before each test method.
     * Initializes fresh game state with player, question, and mock dependencies
     * to ensure test isolation and consistent starting conditions.
     * 
     * @throws Exception if mock object initialization fails
     */
    @BeforeEach
    public void setup() throws Exception {
        player = new Player("P1", "Carl");

        question = new Question("Math", 100, "2+2?");
        question.setCorrectAnswer("A");

        logger = new FakeEventLogger();

        scoreboard = new ScoreObserver() {
            @Override
            public void update(Player p) { }
        };
    }

    /**
     * Tests correct answer execution flow and associated score modifications.
     * Validates that correct answers increase player score by question value
     * and that undo operations properly revert score changes.
     */
    @Test
    public void testAnswerCommandCorrect() {
        AnswerCommand cmd = new AnswerCommand(player, question, "A", logger, scoreboard);
        cmd.execute();

        assertTrue(cmd.isCorrect());
        assertEquals(100, cmd.getDelta());
        assertEquals(100, player.getScore());

        cmd.undo();
        assertEquals(0, player.getScore());
    }

    /**
     * Tests incorrect answer execution flow and penalty score modifications.
     * Validates that incorrect answers decrease player score by question value
     * and that undo operations properly restore points after penalties.
     */
    @Test
    public void testAnswerCommandIncorrect() {
        AnswerCommand cmd = new AnswerCommand(player, question, "B", logger, scoreboard);
        cmd.execute();

        assertFalse(cmd.isCorrect());
        assertEquals(-100, cmd.getDelta());
        assertEquals(-100, player.getScore());

        cmd.undo();
        assertEquals(0, player.getScore());
    }

    /**
     * Tests RemoteControl command history management and undo stack functionality.
     * Validates LIFO (Last-In-First-Out) command execution and reversal
     * with multiple command sequences affecting player score state.
     */
    @Test
    public void testRemoteControl() {
        RemoteControl remote = new RemoteControl();
        AnswerCommand cmd1 = new AnswerCommand(player, question, "A", logger, scoreboard);
        AnswerCommand cmd2 = new AnswerCommand(player, question, "B", logger, scoreboard);

        remote.executeCommand(cmd1);
        assertEquals(100, player.getScore());

        remote.executeCommand(cmd2);
        assertEquals(0, player.getScore());

        remote.undoLast();
        assertEquals(100, player.getScore());

        remote.undoLast();
        assertEquals(0, player.getScore());
    }
}
