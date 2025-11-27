package com.oopopps.command;

/**
 * Defines the contract for commands that can be executed and undone.
 * Implements the Command pattern to encapsulate operations as objects.
 * 
 * This interface allows for implementing undo/redo functionality
 * and command history management in the game.
 */
public interface Command {
    
    /**
     * Executes the command's primary operation.
     */
    void execute();
    
    /**
     * Reverses the effects of the execute operation.
     */
    void undo();
    
    /**
     * Returns a description of what this command does.
     * 
     * @return a human-readable description of the command
     */
    String getDescription();
}
