package com.main.server;

import com.main.xmlfilter.search.SearchCriteria;
import com.main.xmlfilter.service.ServletLevelException;
import com.main.xmlfilter.service.XmlService;
import com.main.xmlfilter.service.XmlServiceException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles 'search' calls.
 *
 * @author Sergiu Indrie
 */
public class SearchServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchServlet.class);
    private ObjectMapper jsonConverter = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        ServletContext context = req.getServletContext();
        String pathToDataFile = context.getInitParameter(ServerConstants.DMOZ_DATA_FILE);
        InputStream inputStream = new FileInputStream(pathToDataFile);

        // obtain request parameters
        int page;
        SearchCriteria searchCriteria;
        try {
            page = ParameterExtractor.getPage(req, resp);
            searchCriteria = getSearchCriteria(req, resp);
        } catch (ServletLevelException e) {
            // all error handling is done, simply return
            return;
        }

        String searchedPageContent;
        try {
            log.info("Searching page " + page + " with search criteria:\n" + searchCriteria);
            searchedPageContent = XmlService.search(searchCriteria, page, inputStream);
        } catch (XmlServiceException e) {
            log.error("An error occurred while processing the search command.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if (searchedPageContent == null || searchedPageContent.trim().isEmpty()) {
            log.warn("The search result is empty");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "The search result is empty");
            return;
        }

         // set cache header
        HttpUtils.addMaxAgeCache(resp, HttpUtils.RESOURCE_MAX_AGE);

        resp.getOutputStream().print(searchedPageContent);
    }

    private SearchCriteria getSearchCriteria(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletLevelException {
        SearchCriteria searchCriteria = jsonConverter.readValue(req.getInputStream(), SearchCriteria.class);
        if (searchCriteria == null) {
            log.error("Invalid search criteria found in request.");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The search call must have a valid search criteria in JSON format in the request body.");
            throw new ServletLevelException();
        }
        return searchCriteria;
    }
}
