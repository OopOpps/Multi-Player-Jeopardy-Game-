package com.oopopps;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Jeopardy question with category, value, text, options, and correct answer.
 * Serves as the core data model for game questions.
 */
public class Question {
    private String category;
    private int value;
    private String questionText;
    private Map<String, String> options = new LinkedHashMap<>();
    private String correctAnswer;

    /**
     * Default constructor for Question.
     */
    public Question() {}

    /**
     * Constructs a Question with basic information.
     * 
     * @param category the question category
     * @param value the point value of the question
     * @param questionText the text of the question
     */
    public Question(String category, int value, String questionText) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
    }

    // getters and setters
    
    /**
     * Gets the question category.
     * 
     * @return the category
     */
    public String getCategory() { return category; }
    
    /**
     * Sets the question category.
     * 
     * @param category the category to set
     */
    public void setCategory(String category) { this.category = category; }
    
    /**
     * Gets the question point value.
     * 
     * @return the point value
     */
    public int getValue() { return value; }
    
    /**
     * Sets the question point value.
     * 
     * @param value the point value to set
     */
    public void setValue(int value) { this.value = value; }
    
    /**
     * Gets the question text.
     * 
     * @return the question text
     */
    public String getQuestionText() { return questionText; }
    
    /**
     * Sets the question text.
     * 
     * @param questionText the question text to set
     */
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    /**
     * Gets the answer options as a map of option keys to option text.
     * 
     * @return the options map
     */
    public Map<String,String> getOptions() { return options; }
    
    /**
     * Sets the answer options.
     * 
     * @param options the options map to set
     */
    public void setOptions(Map<String,String> options) { this.options = options; }
    
    /**
     * Gets the correct answer.
     * 
     * @return the correct answer
     */
    public String getCorrectAnswer() { return correctAnswer; }
    
    /**
     * Sets the correct answer.
     * 
     * @param correctAnswer the correct answer to set
     */
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    /**
     * Returns a string representation of the question.
     * 
     * @return a formatted string showing category, value, and question text
     */
    @Override
    public String toString() {
        return String.format("[%s] %d - %s", category, value, questionText);
    }
}
