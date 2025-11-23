package com.oopopps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLQuestionParser implements QuestionParser {
    @Override
    public List<Question> parse(Path file) throws Exception {
        List<Question> list = new ArrayList<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file.toString());
        
        NodeList questionNodes = doc.getElementsByTagName("QuestionItem");
        
        for (int i = 0; i < questionNodes.getLength(); i++) {
            Node questionNode = questionNodes.item(i);
            if (questionNode.getNodeType() != Node.ELEMENT_NODE) continue;
            
            Element questionElement = (Element) questionNode;
            Question q = new Question();
            
            // Get basic fields
            q.setCategory(getElementText(questionElement, "Category"));
            q.setValue(Integer.parseInt(getElementText(questionElement, "Value")));
            q.setQuestionText(getElementText(questionElement, "QuestionText"));
            q.setCorrectAnswer(getElementText(questionElement, "CorrectAnswer"));
            
            // Handle options
            NodeList optionNodes = questionElement.getElementsByTagName("Options");
            if (optionNodes.getLength() > 0) {
                Element optionsElement = (Element) optionNodes.item(0);
                Map<String, String> options = new HashMap<>();
                
                NodeList children = optionsElement.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element optionElement = (Element) child;
                        String optionName = optionElement.getTagName();
                        String optionValue = optionElement.getTextContent();
                        options.put(optionName, optionValue);
                    }
                }
                q.setOptions(options);
            }
            
            list.add(q);
        }
        
        return list;
    }
    
    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        Element element = (Element) nodes.item(0);
        return element.getTextContent();
    }
}