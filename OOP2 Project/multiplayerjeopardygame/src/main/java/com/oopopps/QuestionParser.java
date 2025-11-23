package com.oopopps;

import java.nio.file.Path;
import java.util.List;

//this interface defines the contract for parsing questions from a file
public interface QuestionParser {
    List<Question> parse(Path file) throws Exception;
}