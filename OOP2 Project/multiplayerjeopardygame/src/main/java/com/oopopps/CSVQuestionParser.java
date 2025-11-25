package com.oopopps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.nio.file.Files;

public class CSVQuestionParser implements QuestionParser {

    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();

        String fileName = file.getFileName().toString();
        BufferedReader r = null;

        // 1️⃣ Try loading from classpath (resources folder)
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            r = new BufferedReader(new InputStreamReader(is));
        } else {
            // 2️⃣ Fallback to filesystem
            Path resolved = file.toAbsolutePath().normalize();

            if (!Files.exists(resolved)) {
                throw new Exception("CSV file not found in resources or filesystem: " + fileName);
            }

            r = new BufferedReader(new FileReader(resolved.toString()));
        }

        // Read header
        String headerLine = r.readLine();
        if (headerLine == null) {
            r.close();
            throw new Exception("CSV file is empty: " + fileName);
        }

        String[] headers = splitCSV(headerLine);

        String line;
        while ((line = r.readLine()) != null) {

            if (line.trim().isEmpty())
                continue;

            String[] fields = splitCSV(line);
            if (fields.length < headers.length)
                continue;

            Question q = new Question();
            Map<String, String> opts = new LinkedHashMap<>();

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                String value = fields[i].trim();

                switch (header) {
                    case "Category":
                        q.setCategory(value);
                        break;

                    case "Value":
                        try {
                            q.setValue(Integer.parseInt(value));
                        } catch (Exception e) {
                            q.setValue(0);
                        }
                        break;

                    case "Question":
                    case "QuestionText":
                        q.setQuestionText(value);
                        break;

                    case "CorrectAnswer":
                        q.setCorrectAnswer(value);
                        break;

                    default:
                        if (header.toLowerCase().startsWith("option") || header.matches("[A-D]")) {
                            opts.put(header, value);
                        }
                        break;
                }
            }

            q.setOptions(opts);
            list.add(q);
        }

        r.close();
        return list;
    }


    // Safe CSV splitting that supports quoted text
    private String[] splitCSV(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}
