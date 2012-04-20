package com.main.xmlfilter.service;

import com.main.xmlfilter.commands.GetPageCommand;
import com.main.xmlfilter.config.Config;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Service used by the servlets
 *
 * @author Sergiu Indrie
 */
public class XmlService {

    private static Logger log = Logger.getLogger(XmlService.class);

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


}
