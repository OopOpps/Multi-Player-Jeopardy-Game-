package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for Question entity validation and data management.
 * Tests core question functionality including category assignment, value setting,
 * text content management, and correct answer configuration.
 * Ensures Question class maintains data integrity and supports flexible
 * question creation for diverse quiz content across multiple domains.
 */
public class QuestionTest {

    /**
     * Tests basic Question object construction and property assignment.
     * Validates that all question attributes can be set and retrieved correctly
     * using standard setter and getter methods. Ensures data persistence
     * and proper object state management for physics domain questions.
     */
    @Test
    public void testQuestionCreation() {
        Question q = new Question();
        
        q.setCategory("Physics");
        q.setValue(100);
        q.setQuestionText("What is torque?");
        q.setCorrectAnswer("B");
    
        assertEquals("Physics", q.getCategory());
        assertEquals(100, q.getValue());
        assertEquals("What is torque?", q.getQuestionText());
        assertEquals("B", q.getCorrectAnswer());
    }

    /**
     * Tests Question object functionality with alternative data values and domain.
     * Validates that the Question class supports diverse content across different
     * subjects and point values. Ensures property independence and demonstrates
     * flexibility in question configuration for comprehensive quiz coverage.
     */
    @Test
    public void testQuestionWithDifferentData() {
        Question q = new Question();

        q.setCategory("Chemistry");
        q.setValue(200);
        q.setQuestionText("What is an electron?");
        q.setCorrectAnswer("C");
        
        assertEquals("Chemistry", q.getCategory());
        assertEquals(200, q.getValue());
        assertEquals("What is an electron?", q.getQuestionText());
        assertEquals("C", q.getCorrectAnswer());
    }
}