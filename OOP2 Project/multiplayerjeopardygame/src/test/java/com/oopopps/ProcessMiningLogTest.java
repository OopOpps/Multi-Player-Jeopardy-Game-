package com.oopopps;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProcessMiningLogTest {

    @TempDir
    Path tempDir;

    @Test
    void testLogFormat() throws Exception {
        Path logFile = tempDir.resolve("process_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "CASE-123");
     
        logger.log("P1", "Start Game", null, null, null, "", 0);
        logger.log("P1", "Select Category", "Science", "100", null, "", 0);
        logger.log("P1", "Answer Question", "Science", "100", "A", "CORRECT", 100);
        logger.log(null, "Generate Report", null, null, null, "", 0);
        logger.log("P1", "Exit Game", null, null, null, "", 100);
        logger.close();
        
        String content = Files.readString(logFile);
        String[] lines = content.split("\n");
      
        String header = lines[0];
        assertTrue(header.contains("Case_ID") && header.contains("Player_ID") && 
                   header.contains("Activity") && header.contains("Timestamp"));
     
        assertTrue(content.contains("Start Game") && content.contains("Select Category") &&
                   content.contains("Answer Question") && content.contains("Generate Report") &&
                   content.contains("Exit Game"));
   
        assertEquals(6, lines.length);
        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].split(",");
            assertEquals(9, columns.length);
            assertEquals("CASE-123", columns[0]);
        }
    }

    @Test
    void testNullValues() throws Exception {
        Path logFile = tempDir.resolve("null_test_log.csv");
        EventLogger logger = new EventLogger(logFile.toString(), "TEST-CASE");

        logger.log(null, "System Event", null, null, null, "", 0);
        logger.log("P1", "Player Action", "Category", "200", "B", "WRONG", -200);
        logger.close();
        
        String content = Files.readString(logFile);
        assertTrue(content.contains("System Event") && content.contains("Player Action") &&
                  content.contains("WRONG") && content.contains("-200"));
    }
}