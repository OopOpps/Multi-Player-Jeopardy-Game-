package com.oopopps;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EventLoggerTest {

    @TempDir
    Path tempDir;

    @Test
    void testEventLoggerCreation() throws Exception {
        Path logFile = tempDir.resolve("test_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "TEST-GAME");
        assertNotNull(logger);
        logger.close();
        
        assertTrue(Files.exists(logFile));
    }

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