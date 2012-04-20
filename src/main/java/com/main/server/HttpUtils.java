package com.main.server;

import javax.servlet.http.HttpServletResponse;

/**
 * Helper for HTTP specific settings like headers.
 *
 * @author Sergiu Indrie
 */
public class HttpUtils {

    public static long RESOURCE_MAX_AGE = 10;
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String GZIP = "gzip";

    public static void addMaxAgeCache(HttpServletResponse response, long seconds) {
        response.setHeader("Cache-Control", "max-age=" + seconds);
    }
}
