package com.oopopps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import org.json.*;

public class JSONQuestionParser implements QuestionParser {
    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();
        
        // Read entire file as string
        FileReader reader = new FileReader(file.toString());
        StringBuilder content = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            content.append((char) ch);
        }
        reader.close();
        
        // Parse JSON
        JSONObject root = new JSONObject(content.toString());
        
        // Get the questions array
        JSONArray questionsArray;
        if (root.has("JeopardyQuestions")) {
            questionsArray = root.getJSONArray("JeopardyQuestions");
        } 
		else {
            questionsArray = root.getJSONArray("questions");
        }
        
        // Process each question
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject qObj = questionsArray.getJSONObject(i);
            Question q = new Question();
            
            q.setCategory(qObj.getString("Category"));
            q.setValue(qObj.getInt("Value"));
            q.setQuestionText(qObj.getString("QuestionText"));
            q.setCorrectAnswer(qObj.getString("CorrectAnswer"));
            
            // Handle options
            JSONObject optionsObj = qObj.getJSONObject("Options");
            Map<String, String> options = new HashMap<>();
            String[] keys = JSONObject.getNames(optionsObj);
            for (String key : keys) {
                options.put(key, optionsObj.getString(key));
            }
            q.setOptions(options);
            
            list.add(q);
        }
        
        return list;
    }
}