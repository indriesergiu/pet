package com.main.server;

import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.service.ServletLevelException;
import com.main.xmlfilter.service.XmlService;
import com.main.xmlfilter.service.XmlServiceException;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        ServletContext context = req.getServletContext();
        String pathToDataFile = context.getInitParameter(ServerConstants.DMOZ_DATA_FILE);
        InputStream inputStream = new FileInputStream(pathToDataFile);

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

        updateFile(req, resp, pathToDataFile, inputStream, page, pageContent);
    }

    private void updateFile(HttpServletRequest req, HttpServletResponse resp, String pathToDataFile, InputStream inputStream, int page, String pageContent) throws IOException {
        File updateFile = getUpdateFile(req);
        try {
            // consider the need for thread safety
            log.info("Updating page " + page + " with page content:\n" + pageContent);
            Writer writer = new BufferedWriter(new FileWriter(updateFile));
            XmlService.updatePage(page, pageContent, inputStream, writer);
            inputStream.close();
        } catch (XmlServiceException e) {
            log.error("An error occurred while processing the update command.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // check if updated XML is well-formed
        boolean wellFormed;
        try {
            wellFormed = !XmlService.isWellFormed(new FileInputStream(updateFile));
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
        String pageContent = IOUtils.toString(req.getInputStream(), Config.ENCODING);
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
