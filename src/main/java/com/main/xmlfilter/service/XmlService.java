package com.main.xmlfilter.service;

import com.main.xmlfilter.commands.GetPageCommand;
import com.main.xmlfilter.commands.SearchCommand;
import com.main.xmlfilter.commands.UpdatePageCommand;
import com.main.xmlfilter.commands.ValidateWellFormedCommand;
import com.main.xmlfilter.commands.xml.CommandParser;
import com.main.xmlfilter.commands.xml.StAXGenericParser;
import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.search.SearchCriteria;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Service used by the servlets
 *
 * @author Sergiu Indrie
 */
public class XmlService {

    private static final Logger log = Logger.getLogger(XmlService.class);

    // TODO sergiu.indrie - protect shared resource

    public static String getPage(int pageNumber, InputStream inputStream) throws XmlServiceException {
        try {
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

            GetPageCommand cmd = new GetPageCommand(pageNumber, reader);
            cmd.execute();

            inputStream.close();
            return cmd.getRequestedPageContent();

        } catch (Exception e) {
            log.error("An error has occurred while calling getPage with pageNumber=" + pageNumber, e);
            throw new XmlServiceException("Page number " + pageNumber + " could not be obtained.");
        }
    }

    public static void updatePage(int pageNumber, String newPageContent, InputStream inputStream, Writer writer) throws XmlServiceException {
        try {
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

            UpdatePageCommand cmd = new UpdatePageCommand(pageNumber, newPageContent, reader, writer);
            cmd.execute();

            inputStream.close();
            writer.close();
        } catch (Exception e) {
            log.error("An error has occurred while calling updatePage with pageNumber=" + pageNumber, e);
            throw new XmlServiceException("Page number " + pageNumber + " could not be updated.");
        }

    }

    public static boolean isWellFormed(InputStream inputStream) throws XmlServiceException {
        InputStreamReader reader = new InputStreamReader(inputStream);
        ValidateWellFormedCommand cmd = new ValidateWellFormedCommand(reader);
        try {
            inputStream.close();
            reader.close();
        } catch (IOException e) {
            log.error("An error has occurred while calling isWellFormed", e);
            throw new XmlServiceException("The well-formed check could not be successfully performed.");
        }
        return cmd.isWellFormed();
    }

    public static String search(SearchCriteria searchCriteria, int pageNumber, InputStream inputStream) throws XmlServiceException {
        SearchCommand cmd;
        try {
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
            CommandParser commandParser = new StAXGenericParser(reader);
            cmd = new SearchCommand(commandParser, searchCriteria, pageNumber);
            cmd.execute();
            inputStream.close();
        } catch (Exception e) {
            log.error("An error has occurred while calling search with searchCriteria=" + searchCriteria + " and pageNumber=" + pageNumber, e);
            throw new XmlServiceException("The well-formed check could not be successfully performed.");
        }

        return cmd.getRequestedPageContent();
    }
}
