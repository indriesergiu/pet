package com.xmlservices.logic.api.commands;

import com.xmlservices.logic.api.commands.xml.CommandParser;
import com.xmlservices.logic.api.commands.xml.StAXGenericParser;
import com.xmlservices.logic.api.commands.xml.elements.XmlElement;
import com.xmlservices.logic.api.search.SearchCriteria;
import com.xmlservices.logic.config.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Searches an XML file for given items in a search criteria and returns the Xth page containing the search criteria.
 *
 * @author Sergiu Indrie
 */
public class SearchCommand implements Command {

    private int currentPage = 0;
    private int currentLine = 0;

    private List<String> currentPageLines;
    private StringBuilder currentLineContent;

    /**
     * indicates that the searched elements were found in the current page
     */
    private boolean found = false;

    private boolean executionDone = false;

    /**
     * app config
     */
    private Config config = Config.getInstance();

    private CommandParser commandParser;
    private SearchCriteria searchCriteria;
    private int requestedPage;

    private String requestedPageContent;

    public SearchCommand(CommandParser commandParser, SearchCriteria searchCriteria, int requestedPage) {
        this.commandParser = commandParser;
        this.searchCriteria = searchCriteria;
        this.requestedPage = requestedPage;
        currentLineContent = new StringBuilder();
        currentPageLines = new LinkedList<String>();
    }

    @Override
    public void execute() throws Exception {

        XmlElement element = commandParser.getNextElement();

        while (element != null && !executionDone) {

            // add element to current line
            currentLineContent.append(element.toXml());

            switch (element.getType()) {
                case START_DOCUMENT:
                    handleStartDocument();
                    break;
                case START_ELEMENT:
                    handleStartElement(element);
                    break;
                case DATA:
                    handleData(element);
                    break;
            }

            element = commandParser.getNextElement();
        }
    }

    private void handleStartDocument() {
        // complete current line
        currentLine++;
        currentPageLines.add(currentLineContent.toString() + "\n");
        currentLineContent = new StringBuilder();
    }

    private void handleStartElement(XmlElement element) {
        Map<String, String> attributes = element.getAttributes();
        if (attributes.size() > 0) {
            for (String attributeName : attributes.keySet()) {
                if (searchCriteria.matchAttribute(attributes.get(attributeName))) {
                    found = true;
                    break;
                }
            }
        }
    }

    private void handleData(XmlElement element) {
        // perform search
        String data = element.getData();
        if (searchCriteria.matchData(data)) {
            found = true;
        }

        // handle new line
        if (data.contains("\n")) {

            // complete current line
            currentLine++;
            currentPageLines.add(currentLineContent.toString());
            currentLineContent = new StringBuilder();

            // reached the end of current page
            if (currentLine >= config.getXmlPageSize()) {

                // check if this is the requested page
                if (currentPage == requestedPage && found) {
                    requestedPageContent = buildRequestedPageContent();
                    executionDone = true;
                    return;
                }

                // clear the counters and the previous page's lines
                currentLine = 0;
                currentPageLines.clear();

                // add the spaces from the previous page's data element (typically "\n   ") to preserve the element indentation
                if (data.matches("\n +")) {
                    currentLineContent.append(data.substring(1));
                }

                // increment the search page number
                if (found) {
                    currentPage++;
                    found = false;
                }
            }
        }
    }

    private String buildRequestedPageContent() {
        StringBuilder builder = new StringBuilder();
        for (String line : currentPageLines) {
            builder.append(line);
        }
        return builder.toString();
    }

    public String getRequestedPageContent() {
        return requestedPageContent;
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\down\\temp\\content.example.txt";
        String filter = "gam";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

        CommandParser commandParser = new StAXGenericParser(reader);
        SearchCommand cmd = new SearchCommand(commandParser, SearchCriteria.createSearchCriteriaFromValue(filter), 0);
        cmd.execute();

        System.out.println(cmd.getRequestedPageContent());

        inputStream.close();
    }
}
