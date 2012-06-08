package com.xmlservices.server.filter;

import com.xmlservices.server.ServerConstants;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Checks all incoming requests for authentication, if not authenticated sends a redirect to the login page.
 *
 * @author Sergiu Indrie
 */
public class AuthenticationFilter implements Filter {

    private static final Logger log = Logger.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Request entered authentication filter");
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
            if (session != null && session.getAttribute(ServerConstants.USERNAME) != null) {
                log.info(MessageFormat.format("User {0} is authenticated.", session.getAttribute(ServerConstants.USERNAME)));
                filterChain.doFilter(request, servletResponse);
            } else {
                log.info("User is not authenticated. Sending 401.");
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be authenticated to perform this operation.");
            }
        } else {
            log.warn("A non-HTTP request has been received");
        }
    }

    @Override
    public void destroy() {
    }
}
