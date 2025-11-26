package com.oopopps;
import com.oopopps.command.*;
import com.oopopps.display.*;
import com.oopopps.report.*;

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

        // Parser will now correctly load from /resources
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

                System.out.println("\n===================================================================");
                System.out.println("                      " + currentPlayer.getName() + "'s Turn\n");
                System.out.println("Current Score: " + currentPlayer.getScore());
                System.out.println(scoreBoard.render());

                showAvailableQuestions(remainingQuestions);

                System.out.print("Choose a category (or type 'quit' to end game): ");
                String chosenCategory = scanner.nextLine().trim();

                if (chosenCategory.equalsIgnoreCase("quit")) {
                    logger.log(currentPlayer.getId(), "Exit Game", null, null, null, "", currentPlayer.getScore());
                    break;
                }

                System.out.print("Choose a value: ");
                int chosenValue = Integer.parseInt(scanner.nextLine().trim());

                Question selectedQuestion = findQuestion(remainingQuestions, chosenCategory, chosenValue);
                if (selectedQuestion == null) {
                    System.out.println("Question not found. Try again.");
                    continue;
                }

                System.out.println("\nQuestion: " + selectedQuestion.getQuestionText());
                System.out.println("Options:");

                Map<String, String> options = selectedQuestion.getOptions();
                for (String optionKey : options.keySet()) {
                    System.out.println("  " + optionKey + ": " + options.get(optionKey));
                }

                System.out.print("Your answer (A, B, C, etc.): ");
                String playerAnswer = scanner.nextLine().trim().toUpperCase();

                AnswerCommand answerCommand = new AnswerCommand(
                        currentPlayer, selectedQuestion, playerAnswer, logger, scoreBoard
                );

                remoteControl.executeCommand(answerCommand);

                boolean correct = answerCommand.isCorrect();
                int pointsChange = correct ? selectedQuestion.getValue() : -selectedQuestion.getValue();

                String turnResult = String.format(
                        "%s: %s for %d points — %s (%+d points)",
                        currentPlayer.getName(), chosenCategory, chosenValue,
                        correct ? "CORRECT" : "WRONG", pointsChange
                );

                turnHistory.add(turnResult);

                System.out.println("Result: " + (correct ? "Correct!" : "Wrong!"));
                System.out.println("Correct answer: " + selectedQuestion.getCorrectAnswer());
                System.out.println(currentPlayer.getName() + "'s new score: " + currentPlayer.getScore());

                remainingQuestions.remove(selectedQuestion);
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            }

            System.out.println("\n=== GAME OVER ===");
            showFinalScores();
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
        System.out.print("How many players? (1–4): ");
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
            categories.computeIfAbsent(q.getCategory(), k -> new ArrayList<>()).add(q.getValue());
        }

        System.out.println("-------------- AVAILABLE QUESTIONS --------------\n");
        
        for (String category : categories.keySet()) {
            List<Integer> values = categories.get(category);
            Collections.sort(values);
            StringBuilder valueStr = new StringBuilder();
            
            for (Integer val : values) {
                valueStr.append(String.format("%4d", val)); 
            }

            System.out.printf(" %-25s : %s%n", category, valueStr.toString());
            System.out.println();
        }
    }

    private Question findQuestion(List<Question> list, String category, int value) {
        String inputLower = category.toLowerCase();
        return list.stream()
            .filter(q -> q.getCategory().toLowerCase().equals(inputLower) && q.getValue() == value)
            .findFirst()
            .orElse(null);
    }

    private void showFinalScores() {
        System.out.println("Final Scores:");
        for (Player p : players) {
            System.out.println("  " + p.getName() + ": " + p.getScore());
        }

        Player winner = players.stream().max(Comparator.comparingInt(Player::getScore)).get();
        System.out.println("Winner: " + winner.getName() + "!");
    }

    private void generateReports(List<String> turnHistory) {
        try {
            System.out.println("Generating reports...");

            ReportGenerator.generate(
                    Path.of("game_report.txt"),
                    gameId, players, turnHistory,
                    new TextReportStrategy()
            );

            ReportGenerator.generate(
                    Path.of("game_report.pdf"),
                    gameId, players, turnHistory,
                    new PDFReportStrategy()
            );

            System.out.println("Reports generated: game_report.txt, game_report.pdf");
            logger.log(null, "Reports Generated", null, null, null, "", 0);

        } catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
    }
}
