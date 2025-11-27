package com.oopopps.command;

import com.oopopps.Player;
import com.oopopps.Question;
import com.oopopps.EventLogger;
import com.oopopps.display.ScoreObserver;
/**
 * Represents a command for answering a question in the Jeopardy game.
 * Implements the Command pattern to encapsulate all information needed
 * to execute and undo a player's answer attempt.
 * 
 * This command handles score calculation, player updates, logging,
 * and scoreboard notifications when executed or undone.
 */

public class AnswerCommand implements Command {
    private final Player player;
    private final Question question;
    private final String givenAnswer;
    private final EventLogger logger;
    private final ScoreObserver scoreboard;

    private boolean executed = false;
    private int delta = 0;
    private boolean correct = false;   // Added so GameEngine can read it
    /**
     * Constructs an AnswerCommand with all necessary dependencies.
     * 
     * @param player the player attempting to answer the question
     * @param question the question being answered
     * @param givenAnswer the answer provided by the player
     * @param logger the event logger for recording game actions
     * @param scoreboard the score observer for updating display
     */

    public AnswerCommand(Player player, Question question, String givenAnswer,
                         EventLogger logger, ScoreObserver scoreboard) {

        this.player = player;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.logger = logger;
        this.scoreboard = scoreboard;
    }
    /**
     * Executes the answer command by evaluating the player's answer,
     * updating scores, notifying observers, and logging the result.
     * Handles answer normalization for flexible input formats.
     */

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
    /**
     * Reverses the effects of executing this command by reverting
     * score changes and updating observers.
     * Only has effect if the command was previously executed.
     */
    
    @Override
    public void undo() {
        if (!executed) return;

        player.updateScore(-delta);

        if (scoreboard != null)
            scoreboard.update(player);

        executed = false;
    }
    /**
     * Returns a human-readable description of this command.
     * 
     * @return a string describing which player answered which category for how many points
     */
    
    @Override
    public String getDescription() {
        return String.format(
            "%s answers %s for %d",
            player.getName(),
            question.getCategory(),
            question.getValue()
        );
    }
    /**
     * Checks if the player's answer was correct.
     * 
     * @return true if the answer was correct, false otherwise
     */
    
    // === Added getters so GameEngine works ===
    public boolean isCorrect() {
        return correct;
    }
    /**
     * Gets the score change resulting from this answer.
     * 
     * @return the points gained or lost from this answer
     */
    
    public int getDelta() {
        return delta;
    }
}
