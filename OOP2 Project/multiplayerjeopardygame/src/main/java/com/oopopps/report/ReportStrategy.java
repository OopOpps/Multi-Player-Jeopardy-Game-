package com.oopopps.report;

import java.nio.file.Path;
import java.util.List;
import com.oopopps.Player;

/**
 * Defines the contract for generating game reports in various formats.
 * Implements the Strategy pattern to allow different report generation algorithms.
 */
public interface ReportStrategy {
    
    /**
     * Generates a game report at the specified location.
     * 
     * @param path the file path where the report will be saved
     * @param caseId the unique identifier for this game session
     * @param players the list of players who participated
     * @param turns the history of turns taken during the game
     * @throws Exception if report generation fails
     */
    void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception;
}
