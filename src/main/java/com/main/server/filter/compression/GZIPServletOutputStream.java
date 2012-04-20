package com.main.server.filter.compression;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Compresses the servlet output stream with gzip method.
 *
 * @author Sergiu Indrie
 */
public class GZIPServletOutputStream extends ServletOutputStream {

    private GZIPOutputStream gzipOutputStream;

    public GZIPServletOutputStream(ServletOutputStream servletOutputStream) throws IOException {
        gzipOutputStream = new GZIPOutputStream(servletOutputStream);
    }

    @Override
    public void write(int b) throws IOException {
        gzipOutputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        gzipOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        gzipOutputStream.close();
    }
}
