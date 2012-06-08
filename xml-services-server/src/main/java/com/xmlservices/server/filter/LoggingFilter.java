package com.xmlservices.server.filter;

import com.xmlservices.server.ServerConstants;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;

/**
 * Logs incoming requests
 *
 * @author Sergiu Indrie
 */
public class LoggingFilter implements Filter {

    private static final Logger log = Logger.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // configure the logging framework from file
        PropertyConfigurator.configure(filterConfig.getServletContext().getInitParameter(ServerConstants.LOGGING_CONFIG_FILE));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
//            String requestBody = IOUtils.toString(request.getInputStream(), Config.ENCODING);
            // TODO sergiu.indrie - disabled for now, it breaks gzip input 
            String requestBody = "";
            log.info(getRequestLogMessage(request, requestBody));

//            filterChain.doFilter(new RequestWrapper(request, requestBody), servletResponse);
            filterChain.doFilter(request, servletResponse);
        } else {
            log.warn("A non-HTTP request has been received");
        }
    }

    private String getRequestLogMessage(HttpServletRequest request, String requestBody) {
        return MessageFormat.format("Request arrived with URI={0} , URL={1} , method={2} , cookies={3} , servletPath={4} , content={5} , parameters={6}", request.getRequestURI(),
                                    request.getRequestURL(), request.getMethod(), toString(request.getCookies()), request.getServletPath(), requestBody, toString(request.getParameterNames()));
    }

    private String toString(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("{");
        for (Cookie cookie : cookies) {
            builder.append(cookie.getName() + "=" + cookie.getValue());
        }
        builder.append("}");

        return builder.toString();
    }

    private String toString(Enumeration<String> values) {
        StringBuilder result = new StringBuilder();
        while (values.hasMoreElements()) {
            result.append(values.nextElement() + " , ");
        }
        return result.toString();
    }

    @Override
    public void destroy() {
    }

}
