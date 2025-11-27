package com.oopopps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for file parsing functionality and Question data model.
 * Validates multiple file format parsers (CSV, XML, JSON) and ensures proper
 * Question object construction with complete attribute mapping.
 */
class FileParserTest {

    /**
     * Temporary directory for isolated log file testing with automatic cleanup.
     * Ensures test independence and prevents file system pollution between test executions.
     */
    @TempDir
    Path tempDir;

    /**
     * Tests CSV question file parsing with standard comma-separated value format.
     * Validates that CSV parser correctly extracts question metadata, options, 
     * and correct answer from structured tabular data.
     * 
     * @throws Exception if file I/O operations fail or CSV parsing encounters format errors
     */
    @Test
    void testCSVParser() throws Exception {
        Path file = tempDir.resolve("test_questions.csv");
        String content = """
            Category,Value,QuestionText,OptionA,OptionB,OptionC,OptionD,CorrectAnswer
            Math,100,What is 1+1?,2,3,4,5,A
            """;
        Files.writeString(file, content);

        List<Question> questions = new CSVQuestionParser().parse(file.toAbsolutePath());
        assertEquals(1, questions.size());

        Question q = questions.get(0);
        assertEquals("Math", q.getCategory());
        assertEquals(100, q.getValue());
        assertEquals("What is 1+1?", q.getQuestionText());
        assertEquals("A", q.getCorrectAnswer());
    }

    /**
     * Tests XML question file parsing using DOM document object model.
     * Validates hierarchical XML structure parsing with nested option elements
     * and proper text content extraction from XML nodes.
     * 
     * @throws Exception if XML file is malformed, inaccessible, or parsing fails
     */
    @Test
    void testXMLParser() throws Exception {
        Path file = tempDir.resolve("test_questions.xml");  
        String content = """
            <?xml version="1.0" encoding="UTF-8"?>
            <questions>
            <QuestionItem>
                <Category>Math</Category>
                <Value>100</Value>
                <QuestionText>What is 1 + 1?</QuestionText>
                <Options>
                <A>1</A>
                <B>2</B>
                <C>3</C>
                <D>4</D>
                </Options>
                <CorrectAnswer>B</CorrectAnswer>
            </QuestionItem>
            </questions>
            """;
        Files.writeString(file, content);
        List<Question> questions = new XMLQuestionParser().parse(file);
        assertEquals(1, questions.size());

        Question q = questions.get(0);
        assertEquals("Math", q.getCategory());
        assertEquals(100, q.getValue());
        assertEquals("What is 1 + 1?", q.getQuestionText());
        assertEquals("B", q.getCorrectAnswer());
    }

    /**
     * Tests JSON question file parsing with JavaScript Object Notation format.
     * Validates JSON object mapping, nested option objects, and array structure
     * handling for multiple choice questions.
     * 
     * @throws Exception if JSON syntax is invalid, file missing, or parsing fails
     */
    @Test
    void testJSONParser() throws Exception {
        Path file = tempDir.resolve("test_questions.json");
        String content = """
        [
            {
                "Category": "Math",
                "Value": 100,
                "QuestionText": "What is 1 + 1?",
                "Options": {"A": "0", "B": "1", "C": "2", "D": "3"},
                "CorrectAnswer": "C"
            }
        ]
        """;
        Files.writeString(file, content);

        List<Question> questions = new JSONQuestionParser().parse(file.toAbsolutePath());
        assertEquals(1, questions.size());

        Question q = questions.get(0);
        assertEquals("Math", q.getCategory());
        assertEquals(100, q.getValue());
        assertEquals("What is 1 + 1?", q.getQuestionText());
        assertEquals("C", q.getCorrectAnswer());
        assertEquals("0", q.getOptions().get("A"));
        assertEquals("1", q.getOptions().get("B"));
        assertEquals("2", q.getOptions().get("C"));
        assertEquals("3", q.getOptions().get("D"));
    }

    /**
     * Tests basic Question class functionality and data integrity.
     * Validates constructor initialization, property accessors/mutators,
     * and option management through Map operations.
     */
    @Test
    void testQuestionClass() {
        Question q = new Question("Test", 100, "Test question?");
        q.setCorrectAnswer("A");
        q.getOptions().put("A", "Answer A");

        assertEquals("Test", q.getCategory());
        assertEquals(100, q.getValue());
        assertEquals("Test question?", q.getQuestionText());
        assertEquals("A", q.getCorrectAnswer());
        assertEquals("Answer A", q.getOptions().get("A"));
    }
}
