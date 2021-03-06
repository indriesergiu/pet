package com.xmlservices.server.servlet;

import com.xmlservices.logic.api.XmlServiceException;
import com.xmlservices.logic.api.XmlServiceFactory;
import com.xmlservices.server.util.HttpUtils;
import com.xmlservices.server.util.ParameterExtractor;
import com.xmlservices.server.ServerConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Handles 'view' calls
 *
 * @author Sergiu Indrie
 */
public class ViewServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ViewServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        ServletContext context = req.getServletContext();
        String pathToDataFile = context.getInitParameter(ServerConstants.DMOZ_DATA_FILE);

        // obtain request parameters
        int page;
        try {
            page = ParameterExtractor.getPage(req, resp);
        } catch (ServletLevelException e) {
            // all error handling is done, simply return
            return;
        }

        String pageContent;

        synchronized (this) {
            InputStream inputStream = new FileInputStream(pathToDataFile);
            try {
                // consider the need for thread safety
                log.info("Obtaining page " + page);
                pageContent = XmlServiceFactory.getXmlService().getPage(page, inputStream);
            } catch (XmlServiceException e) {
                log.error("An error occurred while processing the view command.", e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        if (pageContent == null || pageContent.trim().isEmpty()) {
            log.warn("The view result is empty");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "The view result is empty");
            return;
        }

        log.debug("Page content is \n" + pageContent);

        // set cache header
        HttpUtils.addMaxAgeCache(resp, HttpUtils.RESOURCE_MAX_AGE);

        // set page content in response body
        resp.getOutputStream().print(pageContent);
    }

}
