package com.oopopps.report;

import java.nio.file.Path;
import java.io.PrintWriter;
import java.util.List;
import com.oopoppsPlayer;

//this class implements the ReportStrategy interface to generate text reports
public class TextReportStrategy implements ReportStrategy {
    @Override
    public void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception {
        try (PrintWriter pw = new PrintWriter(path.toFile())) {
            pw.println("JEOPARDY PROGRAMMING GAME REPORT");
            pw.println("================================");
            pw.println();
            pw.println("Case ID: " + caseId);
            pw.println();
            pw.print("Players: ");
            for (int i=0;i<players.size();i++) {
                pw.print(players.get(i).getName());
                if (i<players.size()-1) pw.print(", ");
            }
            pw.println();
            pw.println();
            pw.println("Gameplay Summary:");
            pw.println("-----------------");
            for (String t : turns) pw.println(t);
            pw.println();
            pw.println("Final Scores:");
            for (Player p : players) {
                pw.println(p.getName() + ": " + p.getScore());
            }
        }
    }
}
