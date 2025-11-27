package com.oopopps;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for the EventLogger class functionality
 * Validates CSV log file creation, event recording, and process mining format compliance
 * Ensures proper logging of game activities for behavioral analysis and audit trails
 */
class EventLoggerTest {

    /**
     * Temporary directory for isolated log file testing with automatic cleanup.
     * Ensures test independence and prevents file system pollution between test executions.
     */
    @TempDir
    Path tempDir;

    /**
     * Tests the basic creation and initialization of an EventLogger instance.
     * Verifies that the logger can be instantiated and creates the required CSV log file.
     * 
     * @throws Exception if file system operations fail or logger initialization encounters errors
     */
    @Test
    void testEventLoggerCreation() throws Exception {
        Path logFile = tempDir.resolve("test_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        assertNotNull(logger);
        logger.close();
        
        assertTrue(Files.exists(logFile));
    }

    /**
     * Tests comprehensive event logging functionality across different game activities.
     * Verifies that various game events are properly recorded in the CSV log file
     * with correct data persistence and formatting.
     * 
     * @throws Exception if file I/O operations fail or event logging encounters errors
     */
    @Test
    void testEventLogging() throws Exception {
        Path logFile = tempDir.resolve("test_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        
        logger.log("P1", "Start Game", null, null, null, "", 0);
        logger.log("P1", "Select Category", "Math", "100", null, "", 0);
        logger.log("P1", "Answer Question", "Math", "100", "A", "CORRECT", 100);
        logger.log(null, "Generate Report", null, null, null, "", 0);
        logger.close();
        
        String content = Files.readString(logFile);
        assertTrue(content.contains("TEST-GAME"));
        assertTrue(content.contains("P1"));
        assertTrue(content.contains("Start Game"));
        assertTrue(content.contains("Select Category"));
        assertTrue(content.contains("Answer Question"));
        assertTrue(content.contains("Generate Report"));
        assertTrue(content.contains("CORRECT"));
    }

    /**
     * Tests that the CSV log file contains all required header columns for process mining compatibility.
     * Verifies the presence of standard process mining columns including Case_ID, Player_ID, Activity,
     * Timestamp, and various game-specific metadata fields.
     * 
     * @throws Exception if file reading operations fail or header validation encounters issues
     */
    @Test
    void testEventLoggerHeader() throws Exception {
        Path logFile = tempDir.resolve("test_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        logger.close();
        
        String content = Files.readString(logFile);
        assertTrue(content.contains("Case_ID"));
        assertTrue(content.contains("Player_ID"));
        assertTrue(content.contains("Activity"));
        assertTrue(content.contains("Timestamp"));
        assertTrue(content.contains("Category"));
        assertTrue(content.contains("Question_Value"));
        assertTrue(content.contains("Answer_Given"));
        assertTrue(content.contains("Result"));
        assertTrue(content.contains("Score_After_Play"));
    }
}