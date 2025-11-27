package com.oopopps;

import java.nio.file.Path;
import java.util.List;

/**
 * Defines the contract for parsing questions from various file formats.
 * Implementations handle specific file formats like XML, JSON, and CSV.
 */
public interface QuestionParser {
    
    /**
     * Parses questions from the specified file.
     * 
     * @param file the path to the file containing questions
     * @return a list of Question objects parsed from the file
     * @throws Exception if the file cannot be read or parsed
     */
    List<Question> parse(Path file) throws Exception;
}
