package com.oopopps;

import java.util.Stack;


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
