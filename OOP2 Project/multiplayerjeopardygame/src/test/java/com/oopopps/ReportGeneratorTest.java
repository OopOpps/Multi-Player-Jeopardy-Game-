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


/**
 * Comprehensive test suite for ReportGenerator functionality and Strategy Pattern implementation.
 * Validates multiple report generation strategies including text and PDF formats,
 * ensuring proper content generation, file creation, and edge case handling.
 * Tests the strategic delegation of report formatting to different output strategies.
 */
class ReportGeneratorTest {

    /**
     * Temporary directory for isolated report file testing with automatic cleanup.
     * Ensures test independence and prevents file system pollution between test executions
     * while providing a controlled environment for output file validation.
     */
    @TempDir
    Path tempDir;

    /**
     * Tests text-based report generation using the TextReportStrategy.
     * Validates that text reports contain all required sections including header,
     * case identification, player information, and scoring data in human-readable format.
     * Ensures proper content formatting and structural completeness for text output.
     * 
     * @throws Exception if file I/O operations fail or text generation encounters errors
     */
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

    /**
     * Tests PDF report generation using the PDFReportStrategy.
     * Validates that PDF reports are successfully created as binary files
     * with non-zero content, ensuring the PDF generation process completes
     * without errors and produces valid output files.
     * 
     * @throws Exception if PDF generation fails or file operations encounter errors
     */
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

    /**
     * Tests report generation with minimal data scenarios and empty collections.
     * Validates that the reporting system gracefully handles edge cases where
     * turn history is empty, ensuring robust operation with incomplete data sets
     * and proper default value presentation.
     * 
     * @throws Exception if file operations fail or empty data handling encounters issues
     */
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