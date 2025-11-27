package com.oopopps;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Logs game events to a CSV file for auditing and analysis.
 * Records player actions, question attempts, and score changes with timestamps.
 * Uses a unique case ID to correlate events from the same game session.
 */
public class EventLogger {
    
    private final PrintWriter out;
    private final String caseId;
    
    /**
     * Constructs an EventLogger that writes to the specified file.
     * 
     * @param path the file path where events will be logged
     * @param caseId unique identifier for this game session
     * @throws Exception if the log file cannot be opened
     */
    public EventLogger(String path, String caseId) throws Exception {
        this.out = new PrintWriter(new FileWriter(path, true));
        this.caseId = caseId;
        
        // write header if file new - naive approach: caller should ensure new file or overwrite
        out.println("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play");
        out.flush();
    }
    
    /**
     * Logs a game event with all relevant details.
     * 
     * @param playerId the ID of the player performing the action
     * @param activity the type of activity being performed
     * @param category the question category (if applicable)
     * @param qValue the question value (if applicable)
     * @param answer the answer given by the player (if applicable)
     * @param result the result of the action (Correct/Incorrect)
     * @param scoreAfter the player's score after this action
     */
    public void log(String playerId, String activity, String category, String qValue, String answer, String result, int scoreAfter) {
        String ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        out.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d\n", caseId, playerId==null?"":playerId, activity, ts, category==null?"":category, qValue==null?"":qValue, answer==null?"":answer, result==null?"":result, scoreAfter);
        out.flush();
    }
    
    /**
     * Closes the log file and releases resources.
     */
    public void close() { out.close(); }
}
