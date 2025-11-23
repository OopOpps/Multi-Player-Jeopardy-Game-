package com.oopopps.command;

import java.util.Stack;

//this class manages a stack of commands and provides methods to execute and undo commands
public class RemoteControl {
    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command c) {
        c.execute();
        history.push(c);
    }

    public void undoLast() {
        if (!history.isEmpty()) {
            Command c = history.pop();
            c.undo();
        }
    }
}
