package com.oopopps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
/**
 * Main application class that serves as the entry point for the Jeopardy game.
 * Handles initial setup, user input for question file selection, and game initialization.
 * Displays welcome banner and manages application lifecycle.
 */

public class App {
   /**
     * Main method that starts the Jeopardy game application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {

        
        System.out.println("    \n                          Welcome to                             \n ");
        System.out.println("     ██╗███████╗ ██████╗ ██████╗  █████╗ ██████╗ ██████╗ ██╗   ██╗");
        System.out.println("     ██║██╔════╝██╔═══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝");
        System.out.println("     ██║█████╗  ██║   ██║██████╔╝███████║██████╔╝██║  ██║ ╚████╔╝ ");
        System.out.println("██   ██║██╔══╝  ██║   ██║██╔═══╝ ██╔══██║██╔══██╗██║  ██║  ╚██╔╝  ");
        System.out.println("╚█████╔╝███████╗╚██████╔╝██║     ██║  ██║██║  ██║██████╔╝   ██║   ");
        System.out.println(" ╚════╝ ╚══════╝ ╚═════╝ ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝    ╚═╝   ");
        System.out.println("                                                                   ");


        System.out.println("\n===================================================================");
        System.out.println("                   A MULTIPLAYER JEOPARDY GAME                     ");
        System.out.println("                          BY OOPOPPS                               ");
        System.out.println("===================================================================\n");


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



