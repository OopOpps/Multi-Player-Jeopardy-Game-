package com.oopopps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.oopopps.report.PDFReportStrategy;
import com.oopopps.report.TextReportStrategy;

class ReportGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void testReportGeneratorWithTextStrategy() throws Exception {
        List<Player> players = Arrays.asList(
            new Player("P1", "Vincent"),
            new Player("P2", "Jules")
        );
        List<String> turnHistory = Arrays.asList(
            "Vincent: Math for 100 points — WRONG (-100 points)",
            "Jules: Pop Culture for 200 points — CORRECT (+200 points)"
        );
        
        Path reportPath = tempDir.resolve("test_report.txt");
        
        ReportGenerator.generate(reportPath, "TEST-GAME", players, turnHistory, new TextReportStrategy());
        
        assertTrue(Files.exists(reportPath));
        String content = Files.readString(reportPath);

        assertTrue(content.contains("JEOPARDY PROGRAMMING GAME REPORT"));
        assertTrue(content.contains("Case ID: TEST-GAME"));
        assertTrue(content.contains("Players: Vincent, Jules"));
        assertTrue(content.contains("Final Scores:"));
    }

    @Test
    void testReportGeneratorWithPDFStrategy() throws Exception {
        List<Player> players = Arrays.asList(
            new Player("P1", "Joe"),
            new Player("P2", "Mama")
        );
        List<String> turnHistory = Arrays.asList(
            "Joe: Science for 100 points — CORRECT (+100 points)"
        );
        
        Path reportPath = tempDir.resolve("test_report.pdf");
        
        ReportGenerator.generate(reportPath, "TEST-GAME", players, turnHistory, new PDFReportStrategy());
        
        assertTrue(Files.exists(reportPath));
        assertTrue(Files.size(reportPath) > 0);
    }

    @Test
    void testReportGeneratorWithEmptyData() throws Exception {
        List<Player> players = Arrays.asList(
            new Player("P1", "Johnny")
        );
        List<String> turnHistory = Arrays.asList();
        
        Path reportPath = tempDir.resolve("empty_report.txt");
        ReportGenerator.generate(reportPath, "EMPTY-GAME", players, turnHistory, new TextReportStrategy());
        
        assertTrue(Files.exists(reportPath));
        String content = Files.readString(reportPath);
        
        assertTrue(content.contains("Case ID: EMPTY-GAME"));
        assertTrue(content.contains("Players: Johnny"));
        assertTrue(content.contains("Final Scores:"));
        assertTrue(content.contains("Johnny: 0"));
    }
}