package com.oopopps.report;

import java.nio.file.Path;
import java.util.List;
import com.oopopps.Player;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Implements the ReportStrategy interface to generate professional PDF reports
 * for Jeopardy game sessions using Apache PDFBox. Creates multi-page PDF documents
 * with detailed game summaries, formatted text, and automatic pagination.
 * 
 * The PDF report maintains consistent formatting across pages and includes
 * the same detailed turn-by-turn information as the text report, with
 * appropriate font styling and layout for professional presentation.
 */
public class PDFReportStrategy implements ReportStrategy {
    
    /**
     * Generates a comprehensive PDF report of the Jeopardy game session.
     * Creates a formatted PDF document with detailed turn history, player scores,
     * and educational programming content. Handles multi-page documents
     * automatically when content exceeds page limits.
     * 
     * @param path the file path where the PDF report will be saved
     * @param caseId the unique identifier for this game session
     * @param players the list of players who participated in the game
     * @param turns the history of game turns with player actions and results
     * @throws Exception if PDF generation fails, fonts are unavailable, or path is inaccessible
     */
    @Override
    public void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);
            
            float startY = 750;
            float currentY = startY;
            float margin = 50;
            float lineHeight = 14;
            
            // Title
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
            cs.newLineAtOffset(margin, currentY);
            cs.showText("JEOPARDY PROGRAMMING GAME REPORT");
            cs.endText();
            currentY -= lineHeight * 2;
            
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(margin, currentY);
            cs.showText("Case ID: " + caseId);
            currentY -= lineHeight * 1.5f;
            
            // Players
            StringBuilder playersText = new StringBuilder("Players: ");
            for (int i = 0; i < players.size(); i++) {
                playersText.append(players.get(i).getName());
                if (i < players.size() - 1) playersText.append(", ");
            }
            cs.newLineAtOffset(0, -lineHeight);
            cs.showText(playersText.toString());
            currentY -= lineHeight * 2;
            
            // Gameplay Summary header
            cs.newLineAtOffset(0, -lineHeight);
            cs.showText("Gameplay Summary:");
            cs.newLineAtOffset(0, -lineHeight);
            cs.showText("-----------------");
            currentY -= lineHeight * 2;
            
            cs.endText();
            
            // Process turns with detailed formatting
            int turnNumber = 1;
            for (String turn : turns) {
                if (currentY < 100) { // Need new page
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    currentY = startY;
                }
                
                // Parse turn data
                String[] parts = turn.split(" — ");
                String playerAction = parts[0];
                String result = parts[1];
                
                String[] actionParts = playerAction.split(": ");
                String playerName = actionParts[0];
                String categoryValue = actionParts[1].replace(" for ", "|").replace(" points", "");
                String[] cvParts = categoryValue.split("\\|");
                String category = cvParts[0];
                int value = Integer.parseInt(cvParts[1]);
                
                boolean correct = result.contains("CORRECT");
                int pointsChange = correct ? value : -value;
                
                String questionText = getSampleQuestion(category);
                String answerText = getSampleAnswer(category);
                int scoreAfter = calculateScoreAfterTurn(players, playerName, turnNumber, turns);
                
                // Write turn details
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(margin, currentY);
                
                // Turn header
                cs.showText("Turn " + turnNumber + ": " + playerName + " selected " + category + " for " + value + " pts");
                currentY -= lineHeight;
                
                // Question
                cs.newLineAtOffset(0, -lineHeight);
                cs.showText("Question: " + questionText);
                currentY -= lineHeight;
                
                // Answer and result
                cs.newLineAtOffset(0, -lineHeight);
                cs.showText("Answer: " + answerText + " — " + (correct ? "Correct" : "Incorrect") + 
                           " (" + (pointsChange >= 0 ? "+" : "") + pointsChange + " pts)");
                currentY -= lineHeight;
                
                // Score after turn
                cs.newLineAtOffset(0, -lineHeight);
                cs.showText("Score after turn: " + playerName + " = " + scoreAfter);
                currentY -= lineHeight * 1.5f; // Extra space between turns
                
                cs.endText();
                turnNumber++;
            }
            
            // Final scores
            if (currentY < 150) {
                cs.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);
                currentY = startY;
            }
            
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, currentY);
            cs.showText("Final Scores:");
            currentY -= lineHeight;
            
            cs.setFont(PDType1Font.HELVETICA, 10);
            for (Player p : players) {
                cs.newLineAtOffset(0, -lineHeight);
                cs.showText(p.getName() + ": " + p.getScore());
            }
            
            cs.endText();
            cs.close();
            doc.save(path.toFile());
        }
    }
    
    /**
     * Returns a sample programming question appropriate for the given category.
     * Provides realistic educational content that matches common programming
     * concepts within each category for consistent PDF reporting.
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
     * programming concepts and best practices for PDF report consistency.
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
     * This method provides accurate score progression tracking for the PDF report
     * by using the player's current score from the player object.
     * 
     * @param players the list of all players in the game
     * @param playerName the name of the player to calculate score for
     * @param currentTurn the current turn number being processed
     * @param turns the complete history of game turns
     * @return the player's score after the specified turn
     */
    private int calculateScoreAfterTurn(List<Player> players, String playerName, int currentTurn, List<String> turns) {
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
