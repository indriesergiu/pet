package com.xmlservices.server.servlet;

import com.xmlservices.logic.api.XmlService;
import com.xmlservices.logic.api.XmlServiceException;
import com.xmlservices.logic.api.XmlServiceFactory;
import com.xmlservices.server.util.ParameterExtractor;
import com.xmlservices.server.ServerConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Updates a page of an XML file.
 *
 * @author Sergiu Indrie
 */
public class UpdateServlet extends HttpServlet {

    private static final String SERVLET_TEMP_DIR = "javax.servlet.context.tempdir";
    private static final String UPDATED_FILENAME = "updated.xml";
    private static final Logger log = Logger.getLogger(UpdateServlet.class);
    private static final String ENCODING = "UTF-8";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        ServletContext context = req.getServletContext();
        String pathToDataFile = context.getInitParameter(ServerConstants.DMOZ_DATA_FILE);

        // obtain request parameters
        int page;
        String pageContent;
        try {
            page = ParameterExtractor.getPage(req, resp);
            pageContent = getPageContent(req, resp);
        } catch (ServletLevelException e) {
            // all error handling is done, simply return
            return;
        }

        updateFile(req, resp, pathToDataFile, page, pageContent);
    }

    private synchronized void updateFile(HttpServletRequest req, HttpServletResponse resp, String pathToDataFile, int page, String pageContent) throws IOException {
        File updateFile = getUpdateFile(req);

        InputStream inputStream = new FileInputStream(pathToDataFile);
        XmlService xmlService = XmlServiceFactory.getXmlService();
        try {
            // consider the need for thread safety
            log.info("Updating page " + page + " with page content:\n" + pageContent);
            Writer writer = new BufferedWriter(new FileWriter(updateFile));
            xmlService.updatePage(page, pageContent, inputStream, writer);
            inputStream.close();
            writer.close();
        } catch (XmlServiceException e) {
            log.error("An error occurred while processing the update command.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // check if updated XML is well-formed
        boolean wellFormed;
        try {
            wellFormed = !xmlService.isWellFormed(new FileInputStream(updateFile));
        } catch (XmlServiceException e) {
            log.error("The updated file is not well-formed");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page content. Applying the new page content must result in a well-formed XML file.");
            return;
        }

        if (wellFormed) {
            log.error("The updated file is not well-formed");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page content. Applying the new page content must result in a well-formed XML file.");
            return;
        }

        // overwrite the old page with the updated page
        // consider the need for thread safety
        File dataFile = new File(pathToDataFile);
        if (!dataFile.delete()) {
            log.error("The old DMOZ data file could not be deleted.");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        FileUtils.moveFile(updateFile, dataFile);

        log.info("Update complete.");
    }

    private String getPageContent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletLevelException {
        String pageContent = IOUtils.toString(req.getInputStream(), ENCODING);
        if (pageContent == null) {
            log.error("Page content parameter is not included");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body must contain the new page's content.");
            throw new ServletLevelException();
        }
        return pageContent;
    }

    private File getUpdateFile(HttpServletRequest req) {
        File tempDirectory = (File) req.getServletContext().getAttribute(SERVLET_TEMP_DIR);
        String pathToUpdateFile = tempDirectory.getPath() + getPathSeparator() + UPDATED_FILENAME;
        log.debug("The path to update file is " + pathToUpdateFile);
        return new File(pathToUpdateFile);
    }

    private String getPathSeparator() {
        return "\\";
    }
}
