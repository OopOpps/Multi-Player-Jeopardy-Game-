package com.opopps.report;

import java.nio.file.Path;
import java.util.List;
import com.oopopps.Player;

//this interface defines the contract for generating reports
public interface ReportStrategy {
    void generate(Path path, String caseId, List<Player> players, List<String> turns) throws Exception;
}
