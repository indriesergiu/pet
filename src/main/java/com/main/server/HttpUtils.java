package com.main.server;

import javax.servlet.http.HttpServletResponse;

/**
 * Helper for HTTP specific settings like headers.
 *
 * @author Sergiu Indrie
 */
public class HttpUtils {

    public static void addMaxAgeCache(HttpServletResponse response, long seconds) {
        response.setHeader("Cache-Control", "max-age=" + seconds);
    }
}
