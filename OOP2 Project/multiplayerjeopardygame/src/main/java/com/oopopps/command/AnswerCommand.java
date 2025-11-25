package com.oopopps.command;

import com.oopopps.Player;
import com.oopopps.Question;
import com.oopopps.EventLogger;
import com.oopopps.display.ScoreObserver;

public class AnswerCommand implements Command {
    private final Player player;
    private final Question question;
    private final String givenAnswer;
    private final EventLogger logger;
    private final ScoreObserver scoreboard;

    private boolean executed = false;
    private int delta = 0;
    private boolean correct = false;   // Added so GameEngine can read it

    public AnswerCommand(Player player, Question question, String givenAnswer,
                         EventLogger logger, ScoreObserver scoreboard) {

        this.player = player;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.logger = logger;
        this.scoreboard = scoreboard;
    }

    @Override
    public void execute() {

        // Normalize: allow either "A" or "OptionA"
        String normalizedCorrect = question.getCorrectAnswer().trim().toUpperCase();
        String normalizedGiven = givenAnswer.trim().toUpperCase();

        correct =
            normalizedCorrect.equals(normalizedGiven) ||
            normalizedCorrect.equals("OPTION" + normalizedGiven);

        delta = correct ? question.getValue() : -question.getValue();

        player.updateScore(delta);

        if (scoreboard != null)
            scoreboard.update(player);

        if (logger != null)
            logger.log(
                player.getId(),
                "Answer Question",
                question.getCategory(),
                String.valueOf(question.getValue()),
                givenAnswer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
            );

        executed = true;
    }

    @Override
    public void undo() {
        if (!executed) return;

        player.updateScore(-delta);

        if (scoreboard != null)
            scoreboard.update(player);

        executed = false;
    }

    @Override
    public String getDescription() {
        return String.format(
            "%s answers %s for %d",
            player.getName(),
            question.getCategory(),
            question.getValue()
        );
    }

    // === Added getters so GameEngine works ===
    public boolean isCorrect() {
        return correct;
    }

    public int getDelta() {
        return delta;
    }
}
