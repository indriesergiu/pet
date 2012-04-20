package com.main.server.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Used to recreate the request input stream and reader so they can be read by other filters or servlets.
 *
 * @author Sergiu Indrie
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private String inputStreamContent;

    public RequestWrapper(HttpServletRequest request, String inputStreamContent) {
        super(request);
        this.inputStreamContent = inputStreamContent;
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
