package com.oopopps;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.oopopps.command.AnswerCommand;
import com.oopopps.display.ScoreObserver;

class AnswerCommandTest {

    @TempDir
    Path tempDir;

    private Question makeSampleQuestion() {
        Question q = new Question();
        q.setCategory("Math");
        q.setValue(100);
        q.setQuestionText("What is 1 + 1?");
        q.setCorrectAnswer("A");

        Map<String, String> options = new LinkedHashMap<>();
        options.put("A", "2");
        options.put("B", "3");
        options.put("C", "4");
        options.put("D", "5");
        q.setOptions(options);

        return q;
    }

    private ScoreObserver testerScoreboard() {
        return player -> {};
    }

    private Player makePlayer(String id, String name) {
        return new Player(id, name);
    }

    @Test
    void correctAnswerUpdatesScore() throws Exception {
        Player player = makePlayer("P1", "Dee");
        Question question = makeSampleQuestion();
        Path logFile = tempDir.resolve("correct.csv");

        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        AnswerCommand command = new AnswerCommand(player, question, "A", logger, testerScoreboard());
        command.execute();

        assertTrue(command.isCorrect(), "Answer should be correct");
        assertEquals(100, command.getDelta(), "Delta should be +100 for correct answer");
        assertEquals(100, player.getScore(), "Player score should increase by 100");
        logger.close();
    }

    @Test
    void incorrectAnswerUpdatesScore() throws Exception {
        Player player = makePlayer("P2", "Frank");
        Question question = makeSampleQuestion();
        Path logFile = tempDir.resolve("incorrect.csv");

        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        AnswerCommand command = new AnswerCommand(player, question, "B", logger, testerScoreboard());
        command.execute();

        assertFalse(command.isCorrect(), "Answer should be incorrect");
        assertEquals(-100, command.getDelta(), "Delta should be -100 for incorrect answer");
        assertEquals(-100, player.getScore(), "Player score should decrease by 100");
        logger.close();
    }

    @Test
    void undoRevertsScore() throws Exception {
        Player player = makePlayer("P3", "Charlie");
        Question question = makeSampleQuestion();
        Path logFile = tempDir.resolve("undo.csv");

        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        AnswerCommand command = new AnswerCommand(player, question, "A", logger, testerScoreboard());
        command.execute();
        assertEquals(100, player.getScore(), "Score should be 100 after correct answer");

        command.undo();
        assertEquals(0, player.getScore(), "Score should revert to 0 after undo");
        logger.close();
    }

    @Test
    void descriptionContainsPlayerAndQuestionInfo() throws Exception {
        Player player = makePlayer("P4", "Dennis");
        Question question = makeSampleQuestion();
        Path logFile = tempDir.resolve("desc.csv");

        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        AnswerCommand command = new AnswerCommand(player, question, "A", logger, testerScoreboard());
        String description = command.getDescription();

        assertTrue(description.contains("Dennis"), "Description should contain player name");
        assertTrue(description.contains("Math"), "Description should contain question category");
        assertTrue(description.contains("100"), "Description should contain question value");
        logger.close();
    }
}
