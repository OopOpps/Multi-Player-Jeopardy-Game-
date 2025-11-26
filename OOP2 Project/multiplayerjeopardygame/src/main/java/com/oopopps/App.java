package com.oopopps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        
            System.out.println("===================================================");
            System.out.println("                 J E O P A R D Y                 ");
            System.out.println("                      G A M E                     ");
            System.out.println("===================================================")

        Scanner inputScanner = new Scanner(System.in);
        
        System.out.println("Please type the path to your question file (XML, JSON, or CSV)");
        System.out.println("Or just press Enter to use the default file: ");
        String userInput = inputScanner.nextLine();
        
        Path filePath;
        
        if (userInput.isEmpty()) {
            filePath = Paths.get("src/main/resources/sample_game_XML.xml");
            System.out.println("Using default file: " + filePath);
        } 
        else {
            filePath = Paths.get(userInput);
            System.out.println("Using your file: " + filePath);
        }
        
        try {
            GameEngine game = new GameEngine(filePath);
            game.run();
        } 
        catch (Exception error) {
            System.out.println("Sorry, something went wrong!");
            System.out.println("Error: " + error.getMessage());
            error.printStackTrace();
        } 
        finally {
            inputScanner.close();
            System.out.println("Game ended. Thanks for playing!");
        }
    }
}


