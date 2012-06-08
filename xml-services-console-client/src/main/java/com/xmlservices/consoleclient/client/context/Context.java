package com.xmlservices.consoleclient.client.context;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Http conversation context. Holds inter-request data such as cookies.
 *
 * @author Sergiu Indrie
 */
public class Context {

    private Map<String, String> cookies;
    private Map<String, CacheEntry> cache;
    private Logger logger = Logger.getLogger(Context.class);

    public Context() {
        cookies = new HashMap<String, String>();
        cache = new HashMap<String, CacheEntry>();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void addCookie(String name, String value) {
        cookies.put(name, value);
    }

    public void addCookiesFromString(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] split = cookie.split("=");
            if (split.length == 2) {
                addCookie(split[0], split[1]);
            } else {
                logger.warn("Could not store invalid cookie: " + cookie);
            }
        }
    }

    public String getCookiesAsString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry entry : cookies.entrySet()) {
            result.append(entry.getKey() + "=" + entry.getValue() + ";");
        }
        if (result.length() > 0) {
            return result.substring(0, result.length() - 1).toString();
        } else {
            return "";
        }
    }

    public String getCachedResource(String fullUrl) {
        CacheEntry cacheEntry = cache.get(fullUrl);
        if (cacheEntry != null) {
            if (System.currentTimeMillis() < cacheEntry.getExpirationDateInMillis()) {
                logger.info("Returning cached resource for " + fullUrl);
                return cacheEntry.getContent();
            } else {
                logger.info("Cache expired for " + fullUrl);
            }
        }
        return null;
    }

    public void storeResourceInCache(String fullUrl, String cacheControlHeader, String resource) {
        if (cacheControlHeader.matches("max-age=\\d+")) {
            String maxAgeInSeconds = cacheControlHeader.split("=")[1];
            int seconds = Integer.parseInt(maxAgeInSeconds);
            CacheEntry cacheEntry = new CacheEntry();
            cacheEntry.setContent(resource);
            cacheEntry.setExpirationDateInMillis(new Date(System.currentTimeMillis() + (seconds * 1000)).getTime());
            logger.debug("Storing resource " + fullUrl + " in cache for " + seconds + " seconds.");
            cache.put(fullUrl, cacheEntry);
        }
    }
}
