package com.xmlservices.server.filter.compression;

import org.apache.log4j.Logger;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Decompresses the gzipped input stream.
 *
 * @author Sergiu Indrie
 */
public class GZIPServletInputStream extends ServletInputStream {

    private GZIPInputStream gzipInputStream;

    private static final Logger log = Logger.getLogger(GZIPServletInputStream.class);

    public GZIPServletInputStream(ServletInputStream servletInputStream) {
        try {
            gzipInputStream = new GZIPInputStream(servletInputStream);
        } catch (IOException e) {
            log.warn("The inputStream is empty");
        }
    }

    @Override
    public int read() throws IOException {
        if (gzipInputStream != null) {
            return gzipInputStream.read();
        } else {
            log.error("The read method was called when there is not input stream.");
            throw new IOException("The input stream is empty.");
        }
    }
}
