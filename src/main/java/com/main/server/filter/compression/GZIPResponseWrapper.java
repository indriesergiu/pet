package com.main.server.filter.compression;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used to recreate the request input stream and reader so they can be read by other filters or servlets.
 *
 * @author Sergiu Indrie
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    private GZIPServletOutputStream gzipServletOutputStream;
    private PrintWriter printWriter;

    public GZIPResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        gzipServletOutputStream = new GZIPServletOutputStream(response.getOutputStream());
        printWriter = new PrintWriter(gzipServletOutputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return gzipServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }
}
