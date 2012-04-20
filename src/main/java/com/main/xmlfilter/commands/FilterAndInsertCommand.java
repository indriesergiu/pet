package com.main.xmlfilter.commands;

import com.main.xmlfilter.commands.xml.*;
import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.parsers.stax.elements.XmlElement;
import com.main.xmlfilter.search.SearchCriteria;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 *
 * @author sergiu.indrie
 */
public class FilterAndInsertCommand implements Command {

    private SearchCriteria searchCriteria;

    /**
     * the current depth
     */
    private int depth;
    /**
     * the filtered XML's elements
     */
    private Stack<XmlElement> elements;
    /**
     * indicates that the searched element was found in the current node
     */
    private boolean found = false;
    /**
     * tells the depth of the next tag that has to be added into the element stack even if the found element is not true
     */
    private int lastFoundDepth = -1;
    /**
     * tells if the custom node has been inserted
     */
    private boolean customNodeInserted = false;

    /**
     * app config
     */
    private Config config = Config.getInstance();

    private CommandParser commandParser;
    private XmlWriter xmlWriter;
    private OutputStream outputStream;

    public FilterAndInsertCommand(SearchCriteria searchCriteria, CommandParser commandParser, XmlWriter xmlWriter, OutputStream outputStream) {
        this.searchCriteria = searchCriteria;
        this.commandParser = commandParser;
        this.xmlWriter = xmlWriter;
        this.outputStream = outputStream;
        elements = new Stack<XmlElement>();
    }

    @Override
    public void execute() throws Exception {

        XmlElement element = commandParser.getNextElement();

        while (element != null) {
            switch (element.getType()) {
                case START_DOCUMENT:
                    handleStartDocument(element);
                    break;
                case START_ELEMENT:
                    handleStartElement(element);
                    break;
                case DATA:
                    handleData(element);
                    break;
                case END_ELEMENT:
                    handleEndElement(element);
                    break;
                case END_DOCUMENT:
                    handleEndDocument();
                    break;
            }

            element = commandParser.getNextElement();
        }
    }

    private void handleEndDocument() throws Exception {
        xmlWriter.write(elements, outputStream);
    }

    private void handleEndElement(XmlElement element) {
        String qName = XmlUtils.getFullName(element.getPrefix(), element.getLocalName());

        elements.push(element);
        depth--;

        if (depth <= config.getSearchDepth() && !found) {
            // if this depth is not the expected one, remove the one
            if (depth != lastFoundDepth - 1) {
                // we are outside the search level and the filter has not been found, remove this node
                popNode(qName);
            }
        } else if (depth <= config.getSearchDepth() && found) {
            // the search level has been reached and a filter was found, leave the elements in the stack and continue search
            found = false;

            // add the custom node
            if (!customNodeInserted) {
                pushCustomNode();
                customNodeInserted = true;
            }
        }

        // if the depth is the expected one, change it accordingly
        if (depth == lastFoundDepth - 1) {
            lastFoundDepth--;
        }
    }

    private void handleData(XmlElement element) {
        String data = element.getData();
        if (data.contains("\n")) {
            return;
        }

        if (searchCriteria.matchData(data)) {
            found = true;
            lastFoundDepth = depth;
        }
        XmlElement item = XmlElement.createDataElement(data);
        elements.push(item);
    }

    private void handleStartElement(XmlElement element) {
        elements.push(element);

        // if search depth has not been reached, don't start searching
        Map<String, String> attributes = element.getAttributes();
        if (depth >= config.getSearchDepth()) {
            if (attributes.size() > 0) {
                for (String attributeName : attributes.keySet()) {
                    if (searchCriteria.matchAttribute(attributes.get(attributeName))) {
                        found = true;
                        lastFoundDepth = depth;
                        break;
                    }
                }
            }
        }
        depth++;
    }

    private void handleStartDocument(XmlElement element) {
        elements.push(element);
    }

    private void popNode(String qName) {
        boolean nodeFound;
        // remove first node with identical name
        do {
            elements.pop();
            XmlElement element = elements.peek();
            nodeFound = qName.equals(XmlUtils.getFullName(element.getPrefix(), element.getLocalName()));
        } while (!nodeFound);

        elements.pop();
    }

    private void pushCustomNode() {
        String name = config.getInsertionName();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(name + "Attr", name + "Value");
        elements.push(XmlElement.createStartElement(attributes, null, name));
        elements.push(XmlElement.createStartElement(null, null, name + "Child"));
        elements.push(XmlElement.createDataElement(name + "Data"));
        elements.push(XmlElement.createEndElement(null, name + "Child"));
        elements.push(XmlElement.createEndElement(null, name));
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        String filter = "apple";
        String outputFile = "D:\\output.xml";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        CommandParser commandParser = new StAXGenericParser(reader);
        XmlWriter xmlWriter = new StAXXmlWriter();
        Command cmd = new FilterAndInsertCommand(SearchCriteria.createSearchCriteriaFromValue(filter), commandParser, xmlWriter, outputStream);
        cmd.execute();

        inputStream.close();
        outputStream.close();
    }
}
