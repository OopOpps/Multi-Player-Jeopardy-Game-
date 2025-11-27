package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.oopopps.command.Command;
import com.oopopps.command.RemoteControl;

/**
 * Unit test suite for RemoteControl command pattern implementation.
 * Validates command execution history management, undo functionality,
 * and stack-based command sequencing. Tests the core Command Pattern
 * implementation that enables reversible operations and command history
 * tracking within the game system.
 */
class RemoteControlTest {

    /**
     * Test double implementation of Command interface for verifying
     * execution and undo behavior without side effects. Tracks method
     * invocation counts to validate command lifecycle management.
     */
    private static class TestCommand implements Command {
        private int executeCount = 0;
        private int undoCount = 0;
    
        /**
         * Simulates command execution by incrementing execution counter.
         * Provides controlled testing without actual game state modifications.
         */
        @Override
        public void execute() {
            executeCount++;
        }
                
        /**
         * Simulates command reversal by incrementing undo counter.
         * Enables undo functionality testing without complex state management.
         */
        @Override
        public void undo() {
            undoCount++;
        }
        
        /**
         * Provides descriptive text for the test command.
         * 
         * @return constant description string for test identification
         */
        @Override
        public String getDescription() {
            return "Test Command";
        }
        
        public int getExecuteCount() { return executeCount; }
        public int getUndoCount() { return undoCount; }
    }

    /**
     * Tests basic command execution functionality through RemoteControl.
     * Validates that commands are properly executed and tracked in the
     * command history without automatic undo invocation.
     */
    @Test
    void testRemoteControlExecute() {
        RemoteControl remote = new RemoteControl();
        TestCommand command = new TestCommand();
        
        remote.executeCommand(command);
        
        assertEquals(1, command.getExecuteCount());
        assertEquals(0, command.getUndoCount());
    }

    /**
     * Tests command execution followed by undo operation.
     * Validates the complete command lifecycle including both execution
     * and reversal through the RemoteControl's undo functionality.
     */
    @Test
    void testRemoteControlUndo() {
        RemoteControl remote = new RemoteControl();
        TestCommand command = new TestCommand();
        
        remote.executeCommand(command);
        remote.undoLast();
        
        assertEquals(1, command.getExecuteCount());
        assertEquals(1, command.getUndoCount());
    }

    /**
     * Tests multiple command sequencing and LIFO (Last-In-First-Out) undo behavior.
     * Validates that command execution order is preserved and undo operations
     * reverse commands in the correct reverse chronological sequence.
     */
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

    /**
     * Tests graceful handling of undo operations on empty command history.
     * Validates that the RemoteControl robustly handles edge cases where
     * undo is invoked without any preceding commands, preventing system
     * failures from invalid operation sequences.
     */
    @Test
    void testRemoteControlUndoEmpty() {
        RemoteControl remote = new RemoteControl();
        
        remote.undoLast();
        assertTrue(true);
    }
}