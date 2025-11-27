package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oopopps.command.AnswerCommand;
import com.oopopps.command.RemoteControl;
import com.oopopps.display.ScoreObserver;

public class GameLogicTest {

    private Player player;
    private Question question;
    private EventLogger logger;
    private ScoreObserver scoreboard;

    private static class FakeEventLogger extends EventLogger {
        public FakeEventLogger() throws Exception {
            super("testlog.csv", "TEST"); 
        }

        @Override
        public void log(String playerId, String activity, String category,
                        String qValue, String answer, String result, int scoreAfter) {
        }
    }

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
