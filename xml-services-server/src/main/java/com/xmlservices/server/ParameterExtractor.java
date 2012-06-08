package com.xmlservices.server;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ParameterExtractor {

    private static final String PAGE = "page";

    private static Logger log = Logger.getLogger(ParameterExtractor.class);

    public static int getPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletLevelException {
        int page;
        try {
            page = Integer.parseInt(req.getParameter(PAGE));
        } catch (NumberFormatException e) {
            log.error("Invalid parameter page=" + req.getParameter(PAGE), e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter '" + PAGE + "' must be a positive integer.");
            throw new ServletLevelException();
        }

        if (page < 0) {
            log.error("Invalid parameter page=" + req.getParameter(PAGE));
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter '" + PAGE + "' must be a positive integer.");
            throw new ServletLevelException();
        }

        return page;
    }
}