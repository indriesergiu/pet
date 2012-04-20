package com.main.server.filter;

import com.main.xmlfilter.config.Config;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Logs incoming requests
 *
 * @author Sergiu Indrie
 */
public class LoggingFilter implements Filter {

    private static Logger log = Logger.getLogger(LoggingFilter.class);

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        BasicConfigurator.configure();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String requestBody = IOUtils.toString(request.getInputStream(), Config.ENCODING);
            log.info(getRequestLogMessage(request, requestBody));

            filterChain.doFilter(new RequestWrapper(request, requestBody), servletResponse);
        } else {
            log.warn("A non-HTTP request has been received");
        }
    }

    private String getRequestLogMessage(HttpServletRequest request, String requestBody) {
        return MessageFormat.format("Request arrived with URI={0} , URL={1} , method={2} , cookies={3} , servletPath={4} , content={5}", request.getRequestURI(), request.getRequestURL(),
                                    request.getMethod(), request.getCookies(), request.getServletPath(), requestBody);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }

}
