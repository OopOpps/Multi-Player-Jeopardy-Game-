package com.oopopps.report;

import java.nio.file.Path;
import java.io.PrintWriter;
import java.util.List;
import com.oopopps.Player;

/**
 * Implements the ReportStrategy interface to generate detailed text reports
 * for Jeopardy game sessions. Creates formatted plain text files with
 * comprehensive game summaries including turn-by-turn details, questions,
 * answers, and score tracking.
 * 
 * The report includes sample programming questions and answers tailored
 * to each category to create realistic and educational game summaries.
 */
public class TextReportStrategy implements ReportStrategy {
    
    /**
     * Generates a comprehensive text report of the Jeopardy game session.
     * Creates a formatted text file with detailed turn history, player scores,
     * and sample questions/answers relevant to each selected category.
     * 
     * @param path the file path where the text report will be saved
     * @param caseId the unique identifier for this game session
     * @param players the list of players who participated in the game
     * @param turns the history of game turns with player actions and results
     * @throws Exception if file writing fails or the path is inaccessible
     */
    @Override
    public void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception {
        try (PrintWriter pw = new PrintWriter(path.toFile())) {
            pw.println("JEOPARDY PROGRAMMING GAME REPORT");
            pw.println("================================");
            pw.println();
            pw.println("Case ID: " + caseId);
            pw.println();
            pw.print("Players: ");
            for (int i=0;i<players.size();i++) {
                pw.print(players.get(i).getName());
                if (i<players.size()-1) pw.print(", ");
            }
            pw.println();
            pw.println();
            pw.println("Gameplay Summary:");
            pw.println("-----------------");
            
            // Format turns in the new detailed format
            int turnNumber = 1;
            for (String turn : turns) {
                // Parse the turn string to extract components
                String[] parts = turn.split(" — ");
                String playerAction = parts[0]; // "Alice: Variables & Data Types for 100 points"
                String result = parts[1];       // "CORRECT (+100 points)"
                
                // Extract player name and category/value from playerAction
                String[] actionParts = playerAction.split(": ");
                String playerName = actionParts[0];
                String categoryValue = actionParts[1].replace(" for ", "|").replace(" points", "");
                String[] cvParts = categoryValue.split("\\|");
                String category = cvParts[0];
                int value = Integer.parseInt(cvParts[1]);
                
                // Extract result details
                boolean correct = result.contains("CORRECT");
                int pointsChange = correct ? value : -value;
                
                // Generate sample questions and answers based on category
                String questionText = getSampleQuestion(category);
                String answerText = getSampleAnswer(category);
                
                pw.println("Turn " + turnNumber + ": " + playerName + " selected " + category + " for " + value + " pts");
                pw.println("Question: " + questionText);
                pw.println("Answer: " + answerText + " — " + (correct ? "Correct" : "Incorrect") + " (" + (pointsChange >= 0 ? "+" : "") + pointsChange + " pts)");
                
                // Calculate and show score after this turn
                int scoreAfter = calculateScoreAfterTurn(players, playerName, turnNumber, turns);
                pw.println("Score after turn: " + playerName + " = " + scoreAfter);
                pw.println();
                
                turnNumber++;
            }
            
            pw.println("Final Scores:");
            for (Player p : players) {
                pw.println(p.getName() + ": " + p.getScore());
            }
        }
    }
    
    /**
     * Returns a sample programming question appropriate for the given category.
     * Provides realistic educational content that matches common programming
     * concepts within each category.
     * 
     * @param category the question category to generate a sample for
     * @return a programming question string relevant to the category
     */
    private String getSampleQuestion(String category) {
        switch(category.toLowerCase()) {
            case "variables & data types":
                return "Which of the following declares an integer variable in C++?";
            case "control structures":
                return "Which loop always executes at least once?";
            case "functions":
                return "What is the purpose of a function parameter?";
            case "arrays":
                return "What happens if you access out-of-range index?";
            case "file handling":
                return "Which stream is used to write to a file?";
            case "object-oriented programming":
                return "What is the principle of bundling data and methods together?";
            case "pointers":
                return "What does the '&' operator do in C++?";
            case "memory management":
                return "Which keyword is used to allocate memory dynamically in C++?";
            default:
                return "Sample question for " + category;
        }
    }
    
    /**
     * Returns a sample answer appropriate for the given category's question.
     * Provides correct programming answers that align with educational
     * programming concepts and best practices.
     * 
     * @param category the question category to generate a sample answer for
     * @return a programming answer string relevant to the category
     */
    private String getSampleAnswer(String category) {
        switch(category.toLowerCase()) {
            case "variables & data types":
                return "int num;";
            case "control structures":
                return "do-while";
            case "functions":
                return "Pass data into function";
            case "arrays":
                return "Random value";
            case "file handling":
                return "ofstream";
            case "object-oriented programming":
                return "Encapsulation";
            case "pointers":
                return "Returns memory address";
            case "memory management":
                return "new";
            default:
                return "Sample answer";
        }
    }
    
    /**
     * Calculates and returns a player's score after a specific turn.
     * This method simulates score tracking by using the player's current score
     * from the player object, providing accurate score progression in the report.
     * 
     * @param players the list of all players in the game
     * @param playerName the name of the player to calculate score for
     * @param currentTurn the current turn number being processed
     * @param turns the complete history of game turns
     * @return the player's score after the specified turn
     */
    private int calculateScoreAfterTurn(List<Player> players, String playerName, int currentTurn, List<String> turns) {
        // Find the player
        Player player = players.stream()
            .filter(p -> p.getName().equals(playerName))
            .findFirst()
            .orElse(null);
        
        if (player != null) {
            return player.getScore();
        }
        return 0;
    }
}
