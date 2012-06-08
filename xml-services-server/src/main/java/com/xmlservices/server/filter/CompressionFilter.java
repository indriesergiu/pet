package com.xmlservices.server.filter;

import com.xmlservices.server.util.HttpUtils;
import com.xmlservices.server.filter.compression.GZIPRequestWrapper;
import com.xmlservices.server.filter.compression.GZIPResponseWrapper;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to compress responses and decompress requests (if this is enabled).
 *
 * @author Sergiu Indrie
 */
public class CompressionFilter implements Filter {

    private static final Logger log = Logger.getLogger(CompressionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            if (!isGzipSupported(request)) {
                log.info("Gzip not supported.");
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                log.info("Gzip is supported. Applying compression/decompression");
                GZIPResponseWrapper gzipResponseWrapper = new GZIPResponseWrapper(response);
                filterChain.doFilter(new GZIPRequestWrapper(request), gzipResponseWrapper);

                // complete the GZIP compression
                gzipResponseWrapper.getOutputStream().flush();
                gzipResponseWrapper.getOutputStream().close();
            }
        } else {
            log.warn("A non-HTTP request has been received");
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isGzipSupported(HttpServletRequest request) {
        String browserEncodings = request.getHeader(HttpUtils.ACCEPT_ENCODING);
        return ((browserEncodings != null) && (browserEncodings.indexOf(HttpUtils.GZIP) != -1));
    }
}
