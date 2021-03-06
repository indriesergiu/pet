package com.xmlservices.server.servlet;

import com.xmlservices.server.ServerConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles 'login' calls
 *
 * @author Sergiu Indrie
 */
public class LoginServlet extends HttpServlet {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "pass";

    private static final Logger log = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {

        String user;
        user = req.getParameter(USERNAME);
        if (user == null) {
            log.error("Invalid parameter user=" + user);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter '" + USERNAME + "' must not be null.");
            return;
        }

        String pass;
        pass = req.getParameter(PASSWORD);
        if (pass == null) {
            log.error("Invalid parameter pass=" + pass);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter '" + PASSWORD + "' must not be null.");
            return;
        }

        if (login(user, pass)) {
            log.info("User " + user + " was successfully authenticated.");
            req.getSession().setAttribute(ServerConstants.USERNAME, user);
            req.getSession().setAttribute(ServerConstants.PASSWORD, pass);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            log.info("User " + user + " failed to be authenticated.");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be authenticated to perform this operation.");
        }
    }

    private boolean login(String user, String pass) {
        Map<String, String> authMap = getUsersMap();

        for (String currentUser : authMap.keySet()) {
            if (currentUser.equals(user) && authMap.get(currentUser).equals(pass)) {
                return true;
            }
        }

        return false;
    }

    private Map<String, String> getUsersMap() {
        // TODO sergiu.indrie - could be loaded from a file, DB or other structure
        Map<String, String> map = new HashMap<String, String>();
        map.put("Guest", "GuestPass");
        map.put("Test", "TestPass");
        return map;
    }

}
