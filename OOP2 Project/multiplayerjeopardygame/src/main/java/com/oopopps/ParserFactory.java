package com.oopopps;

import java.nio.file.Path;

//this class is a factory for creating question parsers based on file type
public class ParserFactory {
    public static QuestionParser getParser(Path p) {
        String suffix = p.toString().toLowerCase();
      
        if (suffix.endsWith(".xml")) return new XMLQuestionParser();
        if (suffix.endsWith(".json")) return new JSONQuestionParser();
        if (suffix.endsWith(".csv")) return new CSVQuestionParser();
        throw new IllegalArgumentException("Unsupported file type: " + p);
    }
}
