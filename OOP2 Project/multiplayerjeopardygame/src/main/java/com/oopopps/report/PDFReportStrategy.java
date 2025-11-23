package com.oopopps.report;

import java.nio.file.Path;
import java.util.List;
import com.uoopopps.Player;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

//this class implements the ReportStrategy interface to generate PDF reports using Apache PDFBox
public class PDFReportStrategy implements ReportStrategy {
    
    @Override
    public void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.newLineAtOffset(50, 700);
            cs.showText("JEOPARDY PROGRAMMING GAME REPORT");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 10);
            cs.newLineAtOffset(50, 680);
            cs.showText("Case ID: " + caseId);
            cs.newLineAtOffset(0, -15);
            cs.showText("Players: ");
            
            for (Player p : players) cs.showText(p.getName() + " ");
            
            cs.newLineAtOffset(0, -20);
            cs.showText("Gameplay Summary:");
            
            for (String t : turns) {
                cs.newLineAtOffset(0, -12);
                String line = t.length()>90? t.substring(0,90)+"..." : t;
                cs.showText(line);
            }
            
            cs.endText();
            cs.close();
            doc.save(path.toFile());
        }
    }
}
