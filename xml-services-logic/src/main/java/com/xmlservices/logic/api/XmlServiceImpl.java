package com.xmlservices.logic.api;

import com.xmlservices.logic.api.commands.xml.CommandParser;
import com.xmlservices.logic.api.commands.xml.StAXGenericParser;
import com.xmlservices.logic.api.search.SearchCriteria;
import com.xmlservices.logic.config.Config;
import com.xmlservices.logic.api.commands.GetPageCommand;
import com.xmlservices.logic.api.commands.SearchCommand;
import com.xmlservices.logic.api.commands.UpdatePageCommand;
import com.xmlservices.logic.api.commands.ValidateWellFormedCommand;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Service used by the servlets
 *
 * @author Sergiu Indrie
 */
class XmlServiceImpl implements XmlService {

    private static final Logger log = Logger.getLogger(XmlServiceImpl.class);

    @Override
    public String getPage(int pageNumber, InputStream inputStream) throws XmlServiceException {
        try {
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

            GetPageCommand cmd = new GetPageCommand(pageNumber, reader);
            cmd.execute();

            return cmd.getRequestedPageContent();

        } catch (Exception e) {
            log.error("An error has occurred while calling getPage with pageNumber=" + pageNumber, e);
            throw new XmlServiceException("Page number " + pageNumber + " could not be obtained.");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("An error has occurred while calling getPage with pageNumber=" + pageNumber, e);
                throw new XmlServiceException("Page number " + pageNumber + " could not be obtained.");
            }
        }
    }

    @Override
    public void updatePage(int pageNumber, String newPageContent, InputStream inputStream, Writer writer) throws XmlServiceException {
        try {
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

            UpdatePageCommand cmd = new UpdatePageCommand(pageNumber, newPageContent, reader, writer);
            cmd.execute();

            inputStream.close();
            writer.close();
        } catch (Exception e) {
            log.error("An error has occurred while calling updatePage with pageNumber=" + pageNumber, e);
            throw new XmlServiceException("Page number " + pageNumber + " could not be updated.");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("An error has occurred while calling updatePage with pageNumber=" + pageNumber, e);
                throw new XmlServiceException("Page number " + pageNumber + " could not be updated.");
            }
        }
    }

    @Override
    public boolean isWellFormed(InputStream inputStream) throws XmlServiceException {
        InputStreamReader reader = new InputStreamReader(inputStream);
        ValidateWellFormedCommand cmd = new ValidateWellFormedCommand(reader);
        try {
            inputStream.close();
            reader.close();
            return cmd.isWellFormed();
        } catch (IOException e) {
            log.error("An error has occurred while calling isWellFormed", e);
            throw new XmlServiceException("The well-formed check could not be successfully performed.");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                log.error("An error has occurred while calling isWellFormed", e);
                throw new XmlServiceException("The well-formed check could not be successfully performed.");
            }
        }
    }

    @Override
    public String search(SearchCriteria searchCriteria, int pageNumber, InputStream inputStream) throws XmlServiceException {
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
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("An error has occurred while calling search with searchCriteria=" + searchCriteria + " and pageNumber=" + pageNumber, e);
                throw new XmlServiceException("The well-formed check could not be successfully performed.");
            }
        }

        return cmd.getRequestedPageContent();
    }
}
