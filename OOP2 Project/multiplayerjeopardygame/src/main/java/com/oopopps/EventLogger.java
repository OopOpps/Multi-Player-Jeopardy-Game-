package com.oopopps;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

//this class logs events to a CSV file
public class EventLogger {
    
    private final PrintWriter out;
    private final String caseId;
    
    // constructor - path is file path, caseId is unique identifier for this game
    public EventLogger(String path, String caseId) throws Exception {
        this.out = new PrintWriter(new FileWriter(path, true));
        this.caseId = caseId;
        
        // write header if file new - naive approach: caller should ensure new file or overwrite
        out.println("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play");
        out.flush();
    }
    // log an event
    public void log(String playerId, String activity, String category, String qValue, String answer, String result, int scoreAfter) {
        String ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        out.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d\n", caseId, playerId==null?"":playerId, activity, ts, category==null?"":category, qValue==null?"":qValue, answer==null?"":answer, result==null?"":result, scoreAfter);
        out.flush();
    }
    public void close() { out.close(); }
}
