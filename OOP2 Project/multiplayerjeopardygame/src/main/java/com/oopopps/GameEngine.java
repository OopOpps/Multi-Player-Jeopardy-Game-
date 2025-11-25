package com.oopopps;

import java.nio.file.Path;
import java.util.*;

/**
 * GameEngine runs the CLI Jeopardy game using design patterns
 */
public class GameEngine {
    private final List<Question> questions;
    private final List<Player> players = new ArrayList<>();
    private final EventLogger logger;
    private final String gameId;
    private final Scanner scanner = new Scanner(System.in);
    private final RemoteControl remoteControl = new RemoteControl();
    private final ScoreBoard scoreBoard = new ScoreBoard();

    public GameEngine(Path questionFile) throws Exception {
        this.gameId = "GAME-" + System.currentTimeMillis();
        QuestionParser parser = ParserFactory.getParser(questionFile);
        this.questions = parser.parse(questionFile);
        this.logger = new EventLogger("game_log.csv", gameId);
        logger.log(null, "Game Started", null, null, null, "", 0);
    }

    public void run() {
        try {
            setupPlayers();
            scoreBoard.initPlayers(players);
            
            List<String> turnHistory = new ArrayList<>();
            int currentPlayerIndex = 0;
            List<Question> remainingQuestions = new ArrayList<>(questions);
            
            while (!remainingQuestions.isEmpty()) {
                Player currentPlayer = players.get(currentPlayerIndex);
                
                System.out.println("\n--- " + currentPlayer.getName() + "'s Turn ---");
                System.out.println("Current Score: " + currentPlayer.getScore());
                
                // Observer Pattern: ScoreBoard displays scores
                System.out.println(scoreBoard.render());
                
                // Show available categories and values
                showAvailableQuestions(remainingQuestions);
                
                // Get player's category choice
                System.out.print("Choose a category (or type 'quit' to end game): ");
                String chosenCategory = scanner.nextLine().trim();
                
                if (chosenCategory.equalsIgnoreCase("quit")) {
                    logger.log(currentPlayer.getId(), "Exit Game", null, null, null, "", currentPlayer.getScore());
                    break;
                }
                
                // Get player's value choice
                System.out.print("Choose a value: ");
                int chosenValue = Integer.parseInt(scanner.nextLine().trim());
                
                // Find the selected question
                Question selectedQuestion = findQuestion(remainingQuestions, chosenCategory, chosenValue);
                if (selectedQuestion == null) {
                    System.out.println("Question not found. Please try again.");
                    continue;
                }
                
                // Ask the question
                System.out.println("\nQuestion: " + selectedQuestion.getQuestionText());
                System.out.println("Options:");
                
                Map<String, String> options = selectedQuestion.getOptions();
                for (String optionKey : options.keySet()) {
                    System.out.println("  " + optionKey + ": " + options.get(optionKey));
                }
                
                System.out.print("Your answer (enter option letter like A, B, C, etc.): ");
                String playerAnswer = scanner.nextLine().trim().toUpperCase();
                
                // COMMAND PATTERN: Use AnswerCommand to handle answering logic
                AnswerCommand answerCommand = new AnswerCommand(currentPlayer, selectedQuestion, 
                                                              playerAnswer, logger, scoreBoard);
                remoteControl.executeCommand(answerCommand);
                
                boolean correct = answerCommand.isCorrect();
                int pointsChange = correct ? selectedQuestion.getValue() : -selectedQuestion.getValue();
                
                // Build turn history
                String turnResult = String.format("%s: %s for %d points - %s (%+d points)", 
                    currentPlayer.getName(), chosenCategory, chosenValue, 
                    correct ? "CORRECT" : "WRONG", pointsChange);
                turnHistory.add(turnResult);
                
                System.out.println("Result: " + (correct ? "Correct!" : "Wrong!"));
                System.out.println("Correct answer was: " + selectedQuestion.getCorrectAnswer());
                System.out.println(currentPlayer.getName() + "'s new score: " + currentPlayer.getScore());
                
                // Remove the used question
                remainingQuestions.remove(selectedQuestion);
                
                // Move to next player
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
            
            // Game over - show results
            System.out.println("\n=== GAME OVER ===");
            showFinalScores();
            
            // STRATEGY PATTERN: Generate reports using different strategies
            generateReports(turnHistory);
            
        } catch (Exception e) {
            System.out.println("Error during game: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            logger.close();
        }
    }

    private void setupPlayers() {
        System.out.print("How many players? (1-4): ");
        int numPlayers = Integer.parseInt(scanner.nextLine().trim());
        
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            String playerId = "P" + i;
            Player player = new Player(playerId, name);
            players.add(player);
            logger.log(playerId, "Player Joined", null, null, name, "", 0);
        }
    }

    private void showAvailableQuestions(List<Question> questions) {
        Map<String, List<Integer>> categories = new LinkedHashMap<>();
        
        for (Question q : questions) {
            String category = q.getCategory();
            int value = q.getValue();
            
            if (!categories.containsKey(category)) {
                categories.put(category, new ArrayList<>());
            }
            categories.get(category).add(value);
        }
        
        System.out.println("Available Questions:");
        for (String category : categories.keySet()) {
            List<Integer> values = categories.get(category);
            Collections.sort(values);
            System.out.println("  " + category + ": " + values);
        }
    }

    private Question findQuestion(List<Question> questions, String category, int value) {
        for (Question q : questions) {
            if (q.getCategory().equals(category) && q.getValue() == value) {
                return q;
            }
        }
        return null;
    }

    private void showFinalScores() {
        System.out.println("Final Scores:");
        for (Player p : players) {
            System.out.println("  " + p.getName() + ": " + p.getScore());
        }
        
        // Find winner
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        System.out.println("Winner: " + winner.getName() + "!");
    }

    private void generateReports(List<String> turnHistory) {
        try {
            // STRATEGY PATTERN: Different report formats
            System.out.println("Generating reports...");
            
            // Text report strategy
            TextReportStrategy textStrategy = new TextReportStrategy();
            ReportGenerator.generate(Path.of("game_report.txt"), gameId, players, turnHistory, textStrategy);
            
            // PDF report strategy  
            PDFReportStrategy pdfStrategy = new PDFReportStrategy();
            ReportGenerator.generate(Path.of("game_report.pdf"), gameId, players, turnHistory, pdfStrategy);
            
            System.out.println("Reports generated: game_report.txt and game_report.pdf");
            logger.log(null, "Reports Generated", null, null, null, "", 0);
            
        } catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
    }
}