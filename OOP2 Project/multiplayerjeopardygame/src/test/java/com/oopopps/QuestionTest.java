package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class QuestionTest {

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