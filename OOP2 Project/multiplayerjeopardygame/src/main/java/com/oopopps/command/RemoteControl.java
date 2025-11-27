package com.oopopps.command;

import java.util.Stack;

/**
 * Manages command execution and undo functionality using a stack-based history.
 * Implements the Command pattern to provide undo capabilities for game actions.
 * 
 * This class maintains a history of executed commands and allows
 * reversing them in LIFO (last-in-first-out) order.
 */
public class RemoteControl {
    private final Stack<Command> history = new Stack<>();

    /**
     * Executes a command and adds it to the history for potential undo.
     * 
     * @param c the command to execute
     */
    public void executeCommand(Command c) {
        c.execute();
        history.push(c);
    }

    /**
     * Undoes the most recently executed command by popping it from
     * the history and calling its undo method.
     * Has no effect if no commands have been executed.
     */
    public void undoLast() {
        if (!history.isEmpty()) {
            Command c = history.pop();
            c.undo();
        }
    }
}
