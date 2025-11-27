package com.oopopps;

import java.nio.file.Path;
import java.util.List;
import com.oopopps.report.*;

/**
 * Generates game reports using the Strategy pattern.
 * Delegates actual report generation to specific strategy implementations.
 */
public class ReportGenerator {
    
    /**
     * Generates a game report using the specified strategy.
     * 
     * @param path the file path where the report will be saved
     * @param caseId the unique identifier for this game session
     * @param players the list of players who participated
     * @param turns the history of turns taken during the game
     * @param strategy the report generation strategy to use
     * @throws Exception if report generation fails
     */
    public static void generate(Path path, String caseId, List<Player> players, List<String> turns, ReportStrategy strategy) throws Exception {
        strategy.generate(path, caseId, players, turns);
    }
}
