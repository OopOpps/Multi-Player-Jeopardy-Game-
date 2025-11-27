package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.oopopps.command.Command;
import com.oopopps.command.RemoteControl;

class RemoteControlTest {

    private static class TestCommand implements Command {
        private int executeCount = 0;
        private int undoCount = 0;
        
        @Override
        public void execute() {
            executeCount++;
        }
        
        @Override
        public void undo() {
            undoCount++;
        }
        
        @Override
        public String getDescription() {
            return "Test Command";
        }
        
        public int getExecuteCount() { return executeCount; }
        public int getUndoCount() { return undoCount; }
    }

    @Test
    void testRemoteControlExecute() {
        RemoteControl remote = new RemoteControl();
        TestCommand command = new TestCommand();
        
        remote.executeCommand(command);
        
        assertEquals(1, command.getExecuteCount());
        assertEquals(0, command.getUndoCount());
    }

    @Test
    void testRemoteControlUndo() {
        RemoteControl remote = new RemoteControl();
        TestCommand command = new TestCommand();
        
        remote.executeCommand(command);
        remote.undoLast();
        
        assertEquals(1, command.getExecuteCount());
        assertEquals(1, command.getUndoCount());
    }

    @Test
    void testRemoteControlMultipleCommands() {
        RemoteControl remote = new RemoteControl();
        TestCommand cmd1 = new TestCommand();
        TestCommand cmd2 = new TestCommand();
        
        remote.executeCommand(cmd1);
        remote.executeCommand(cmd2);
        
        assertEquals(1, cmd1.getExecuteCount());
        assertEquals(1, cmd2.getExecuteCount());
        
        remote.undoLast();
        assertEquals(1, cmd2.getUndoCount());
        
        remote.undoLast();
        assertEquals(1, cmd1.getUndoCount());
    }

    @Test
    void testRemoteControlUndoEmpty() {
        RemoteControl remote = new RemoteControl();
        
        remote.undoLast();
        assertTrue(true);
    }
}