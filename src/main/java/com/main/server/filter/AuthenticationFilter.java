package com.main.server.filter;

import com.main.server.LoginServlet;
import com.main.server.ServerConstants;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Checks all incoming requests for authentication, if not authenticated sends a redirect to the login page.
 *
 * @author Sergiu Indrie
 */
public class AuthenticationFilter implements Filter {

    private static final Logger log = Logger.getLogger(AuthenticationFilter.class);
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        // init session cookies map used for authentication
        filterConfig.getServletContext().setAttribute(ServerConstants.SESSION_COOKIES, new HashMap<String, Cookie>());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Request entered authentication filter");
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            Cookie sessionCookie = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (LoginServlet.SESSION_ID_COOKIE.equals(cookie.getName())) {
                        sessionCookie = cookie;
                        break;
                    }
                }
            }

            if (isValid(servletRequest.getServletContext(), sessionCookie)) {
                log.info("Request has a valid session id cookie.");
                filterChain.doFilter(request, servletResponse);
            } else {
                log.info("Request does not a valid session id cookie. Redirecting to unauthentication page.");
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be authenticated to perform this operation.");
            }

        } else {
            log.warn("A non-HTTP request has been received");
        }
    }

    private boolean isValid(ServletContext servletContext, Cookie sessionCookie) {
        if (sessionCookie == null) {
            return false;
        }

        Map<String, Cookie> sessionCookies = (Map<String, Cookie>) servletContext.getAttribute(ServerConstants.SESSION_COOKIES);
        return sessionCookies.containsKey(sessionCookie.getValue());
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
