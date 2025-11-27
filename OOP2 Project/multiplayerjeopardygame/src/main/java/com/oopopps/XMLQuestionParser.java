package com.oopopps;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 * Parses XML files containing Jeopardy questions into Question objects.
 * Uses DOM parsing to extract question data from structured XML documents.
 */
public class XMLQuestionParser implements QuestionParser {

    /**
     * Parses an XML file and converts it into a list of Question objects.
     * 
     * @param file the path to the XML file to parse
     * @return a list of Question objects parsed from the file
     * @throws Exception if file cannot be read or parsed
     */
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

            // Parse options - Use LinkedHashMap to preserve order
            Map<String, String> options = new LinkedHashMap<>(); // CHANGED: HashMap -> LinkedHashMap
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

    /**
     * Extracts text content from a child element of the given parent element.
     * 
     * @param parent the parent element to search within
     * @param tagName the name of the child element
     * @return the text content of the child element, or empty string if not found
     */
    private String getElementText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return "";
        return list.item(0).getTextContent();
    }
}
