package com.oopopps;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLQuestionParser implements QuestionParser {

    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();

        // Load XML from resources
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream(file.getFileName().toString());

        if (is == null) {
            throw new FileNotFoundException("Resource not found: " + file.getFileName());
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        NodeList questionNodes = doc.getElementsByTagName("QuestionItem");

        for (int i = 0; i < questionNodes.getLength(); i++) {
            Node questionNode = questionNodes.item(i);
            if (questionNode.getNodeType() != Node.ELEMENT_NODE) continue;

            Element questionElement = (Element) questionNode;
            Question q = new Question();

            q.setCategory(getElementText(questionElement, "Category"));
            q.setValue(Integer.parseInt(getElementText(questionElement, "Value")));
            q.setQuestionText(getElementText(questionElement, "QuestionText"));
            q.setCorrectAnswer(getElementText(questionElement, "CorrectAnswer"));

            // Parse options
            Map<String, String> options = new HashMap<>();
            NodeList optionParent = questionElement.getElementsByTagName("Options");
            if (optionParent.getLength() > 0) {
                NodeList children = optionParent.item(0).getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element optionElement = (Element) child;
                        options.put(optionElement.getTagName(), optionElement.getTextContent());
                    }
                }
            }

            q.setOptions(options);
            list.add(q);
        }

        return list;
    }

    private String getElementText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return "";
        return list.item(0).getTextContent();
    }
}
