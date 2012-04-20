package com.main.xmlfilter.commands;

import com.main.xmlfilter.commands.xml.CommandParser;
import com.main.xmlfilter.commands.xml.StAXGenericParser;
import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.parsers.stax.elements.XmlElement;
import com.main.xmlfilter.search.SearchCriteria;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * Searches an XML file for given items in a search criteria and returns the Xth page containing the search criteria.
 *
 * @author Sergiu Indrie
 */
public class SearchCommand implements Command {

    private int currentPage = 0;
    private int currentLine = 0;

    private List<String> currentPageLines;

    /**
     * indicates that the searched element was found in the current node
     */
    private boolean found = false;

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
    }

    @Override
    public void execute() throws Exception {

        XmlElement element = commandParser.getNextElement();

        // TODO sergiu.indrie - user XmlElement.toXml() to write each element (also DATA elements containing '\n' signal newlines)

        while (element != null) {
            switch (element.getType()) {
                case START_DOCUMENT:
//                    handleStartDocument(element);
                    break;
                case START_ELEMENT:
//                    handleStartElement(element);
                    break;
                case DATA:
//                    handleData(element);
                    break;
                case END_ELEMENT:
//                    handleEndElement(element);
                    break;
                case END_DOCUMENT:
//                    handleEndDocument();
                    break;
            }

            element = commandParser.getNextElement();
        }
    }

    public String getRequestedPageContent() {
        return requestedPageContent;
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        String filter = "apple";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

        CommandParser commandParser = new StAXGenericParser(reader);
        Command cmd = new SearchCommand(commandParser, SearchCriteria.createSearchCriteriaFromValue(filter), 1);
        cmd.execute();

        inputStream.close();
    }
}
