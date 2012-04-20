package com.main.server;

import com.main.xmlfilter.service.XmlService;
import com.main.xmlfilter.service.XmlServiceException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Handles 'view' calls
 *
 * @author Sergiu Indrie
 */
public class ViewServlet extends HttpServlet {

    private static final String PAGE = "page";

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ViewServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        ServletContext context = req.getServletContext();
        InputStream inputStream = context.getResourceAsStream(context.getInitParameter(ServerConstants.DMOZ_DATA_FILE));

        int page;
        try {
            page = Integer.parseInt(req.getParameter(PAGE));
        } catch (NumberFormatException e) {
            log.error("Invalid parameter page=" + req.getParameter(PAGE), e);
            // todo send error
            return;
        }

        String pageContent;
        try {
            // consider the need for thread safety
            log.info("Obtaining page " + page);
            pageContent = XmlService.getPage(page, inputStream);
        } catch (XmlServiceException e) {
            log.error("An error occurred while processing the view command.", e);
            // todo send error
            return;
        }

        resp.getOutputStream().print(pageContent);
    }

}
