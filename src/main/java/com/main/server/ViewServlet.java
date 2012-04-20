package com.main.server;

import com.main.xmlfilter.service.ServletLevelException;
import com.main.xmlfilter.service.XmlService;
import com.main.xmlfilter.service.XmlServiceException;
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
        InputStream inputStream = new FileInputStream(pathToDataFile);


        // obtain request parameters
        int page;
        try {
            page = ParameterExtractor.getPage(req, resp);
        } catch (ServletLevelException e) {
            // all error handling is done, simply return
            return;
        }

        String pageContent;
        try {
            // consider the need for thread safety
            log.info("Obtaining page " + page);
            pageContent = XmlService.getPage(page, inputStream);
        } catch (XmlServiceException e) {
            log.error("An error occurred while processing the view command.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // set page content in response body
        resp.getOutputStream().print(pageContent);
    }

}
