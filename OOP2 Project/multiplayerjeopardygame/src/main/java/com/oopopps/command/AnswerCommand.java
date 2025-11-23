package com.oopopps.command;

import com.oopopps.Player;
import com.oopopps.Question;
import com.oopopps.EventLogger;
import com.oopopps.display.ScoreObserver;

//this class implements the Command interface to handle player answers to questions
public class AnswerCommand implements Command {
    private final Player player;
    private final Question question;
    private final String givenAnswer;
    private final EventLogger logger;
    private final ScoreObserver scoreboard;
    private boolean executed = false;
    private int delta = 0;
  
    public AnswerCommand(Player player, Question question, String givenAnswer, EventLogger logger, ScoreObserver scoreboard) {
        this.player = player;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.logger = logger;
        this.scoreboard = scoreboard;
    }

    //this method executes the command by updating the player's score and logging the event
    @Override
    public void execute() {
        boolean correct = question.getCorrectAnswer().trim().equalsIgnoreCase(givenAnswer) || question.getCorrectAnswer().equalsIgnoreCase("Option"+givenAnswer);
        delta = correct ? question.getValue() : -question.getValue();
        player.updateScore(delta);
        if (scoreboard != null) scoreboard.update(player);
        if (logger != null) logger.log(player.getId(), "Answer Question", question.getCategory(), String.valueOf(question.getValue()), givenAnswer, correct?"Correct":"Incorrect", player.getScore());
        executed = true;
    }

    //this method undoes the command by reverting the player's score and updating the scoreboard
    @Override
    public void undo() {
        if (!executed) return;
        player.updateScore(-delta);
        if (scoreboard != null) scoreboard.update(player);
        executed = false;
    }

    @Override
    public String getDescription() {
        return String.format("%s answers %s for %d", player.getName(), question.getCategory(), question.getValue());
    }
}
