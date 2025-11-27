package com.oopopps;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.json.*;

/**
 * Parses JSON files containing Jeopardy questions into Question objects.
 * Supports multiple JSON formats and both classpath resource loading and filesystem access.
 */
public class JSONQuestionParser implements QuestionParser {

    /**
     * Parses a JSON file and converts it into a list of Question objects.
     * 
     * @param file the path to the JSON file to parse
     * @return a list of Question objects parsed from the file
     * @throws Exception if file cannot be read or parsed
     */
    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();

        String fileName = file.getFileName().toString();
        String content;

        // Try loading from resources
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

        if (is != null) {
            content = new String(is.readAllBytes());
        } else {
            // Fallback to filesystem path
            Path resolved = file.toAbsolutePath().normalize();

            if (!Files.exists(resolved)) {
                throw new Exception("JSON file not found in resources or filesystem: " + fileName);
            }

            content = Files.readString(resolved);
        }

        content = content.trim();
        JSONArray arr;

        // NEW: Detect raw JSON array (your file format)
        if (content.startsWith("[")) {
            arr = new JSONArray(content);
        } else {
            JSONObject root = new JSONObject(content);

            if (root.has("JeopardyQuestions")) {
                arr = root.getJSONArray("JeopardyQuestions");
            } else if (root.has("questions")) {
                arr = root.getJSONArray("questions");
            } else {
                throw new Exception("JSON does not contain 'JeopardyQuestions' or 'questions' array");
            }
        }

        // Parse questions
        for (int i = 0; i < arr.length(); i++) {
            JSONObject qObj = arr.getJSONObject(i);
            Question q = new Question();

            q.setCategory(getSafeString(qObj, "Category"));
            q.setValue(getSafeInt(qObj, "Value"));

            // Support both "QuestionText" and "Question"
            if (qObj.has("QuestionText"))
                q.setQuestionText(qObj.getString("QuestionText"));
            else if (qObj.has("Question"))
                q.setQuestionText(qObj.getString("Question"));
            else
                q.setQuestionText("");

            q.setCorrectAnswer(getSafeString(qObj, "CorrectAnswer"));

            Map<String, String> options = new LinkedHashMap<>();
            if (qObj.has("Options")) {
                JSONObject opts = qObj.getJSONObject("Options");

                for (String key : opts.keySet()) {
                    options.put(key, opts.getString(key));
                }
            }

            q.setOptions(options);
            list.add(q);
        }

        return list;
    }

    /**
     * Safely extracts a string value from a JSON object.
     * 
     * @param obj the JSON object to extract from
     * @param key the key to look up
     * @return the string value or empty string if key doesn't exist
     */
    private String getSafeString(JSONObject obj, String key) {
        return obj.has(key) ? obj.getString(key) : "";
    }

    /**
     * Safely extracts an integer value from a JSON object.
     * 
     * @param obj the JSON object to extract from
     * @param key the key to look up
     * @return the integer value or 0 if key doesn't exist
     */
    private int getSafeInt(JSONObject obj, String key) {
        return obj.has(key) ? obj.getInt(key) : 0;
    }
}
