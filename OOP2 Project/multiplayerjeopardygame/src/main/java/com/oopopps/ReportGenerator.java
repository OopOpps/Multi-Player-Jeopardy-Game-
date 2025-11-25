package com.oopopps;

import java.nio.file.Path;
import java.util.List;
import com.oopopps.report.*;

//this class generates reports using the strategy pattern
public class ReportGenerator {
    public static void generate(Path path, String caseId, List<Player> players, List<String> turns, ReportStrategy strategy) throws Exception {
        strategy.generate(path, caseId, players, turns);
    }
}
