package com.main.httpclient.context;

/**
 * HttpClient cache entry.
 *
 * @author Sergiu Indrie
 */
public class CacheEntry {

    private long expirationDateInMillis;
    private String content;

    public long getExpirationDateInMillis() {
        return expirationDateInMillis;
    }

    public void setExpirationDateInMillis(long expirationDateInMillis) {
        this.expirationDateInMillis = expirationDateInMillis;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CacheEntry{" + "expirationDateInMillis=" + expirationDateInMillis + ", content='" + content + '\'' + '}';
    }
}
