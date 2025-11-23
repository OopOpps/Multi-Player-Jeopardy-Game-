package com.oopopps;

import java.util.LinkedHashMap;
import java.util.Map;

//this class represents a question in the game
public class Question {
    private String category;
    private int value;
    private String questionText;
    private Map<String, String> options = new LinkedHashMap<>();
    private String correctAnswer;

    public Question() {}

    public Question(String category, int value, String questionText) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
    }

    // getters and setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public Map<String,String> getOptions() { return options; }
    public void setOptions(Map<String,String> options) { this.options = options; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    @Override
    public String toString() {
        return String.format("[%s] %d - %s", category, value, questionText);
    }
}
