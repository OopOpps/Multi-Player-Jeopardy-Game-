package com.oopopps;

import java.nio.file.Path;

/**
 * Factory class for creating appropriate QuestionParser instances based on file extension.
 * Implements the Factory pattern to decouple parser creation from usage.
 */
public class ParserFactory {
    
    /**
     * Returns the appropriate QuestionParser for the given file based on its extension.
     * 
     * @param p the path to the question file
     * @return a QuestionParser implementation suitable for the file type
     * @throws IllegalArgumentException if the file type is not supported
     */
    public static QuestionParser getParser(Path p) {
        String suffix = p.toString().toLowerCase();
      
        if (suffix.endsWith(".xml")) return new XMLQuestionParser();
        if (suffix.endsWith(".json")) return new JSONQuestionParser();
        if (suffix.endsWith(".csv")) return new CSVQuestionParser();
        throw new IllegalArgumentException("Unsupported file type: " + p);
    }
}
