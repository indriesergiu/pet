package com.xmlservices.jspclient.htmlclient.beans;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class HttpClientException extends Exception {

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
