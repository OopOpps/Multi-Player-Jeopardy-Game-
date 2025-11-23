package com.oopopps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.FileReader;

public class CSVQuestionParser implements QuestionParser {
    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader(file.toString()));
        
        // Read header line
        String headerLine = r.readLine();
        String[] headers = headerLine.split(",");
        
        String line;
        while ((line = r.readLine()) != null) {
            String[] fields = line.split(",");
            
            Question q = new Question();
            Map<String, String> opts = new LinkedHashMap<>();
            
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                String value = fields[i];
                
                if (header.equals("Category")) {
                    q.setCategory(value);
                } 
				else if (header.equals("Value")) {
                    q.setValue(Integer.parseInt(value));
                } 
				else if (header.equals("QuestionText")) {
                    q.setQuestionText(value);
                } 
				else if (header.equals("CorrectAnswer")) {
                    q.setCorrectAnswer(value);
                } 
				else if (header.startsWith("Option")) {
                    opts.put(header, value);
                }
            }
            
            q.setOptions(opts);
            list.add(q);
        }
        
        r.close();
        return list;
    }
}