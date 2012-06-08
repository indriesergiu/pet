package com.xmlservices.server.filter.compression;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Decodes the received request with gzip.
 *
 * @author Sergiu Indrie
 */
public class GZIPRequestWrapper extends HttpServletRequestWrapper {

    private GZIPServletInputStream gzipServletInputStream;
    private BufferedReader bufferedReader;

    public GZIPRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        gzipServletInputStream = new GZIPServletInputStream(request.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(gzipServletInputStream));
    }

    @Override
    public ServletInputStream getInputStream() {
        return gzipServletInputStream;
    }

    @Override
    public BufferedReader getReader() {
        return bufferedReader;
    }
}
