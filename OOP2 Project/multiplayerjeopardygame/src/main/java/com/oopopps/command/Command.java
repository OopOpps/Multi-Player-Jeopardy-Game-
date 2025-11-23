package com.oopopps.command;

//this interface defines the contract for commands that can be executed and undone
public interface Command {
    void execute();
    void undo();
    String getDescription();
}
