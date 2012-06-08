package com.xmlservices.jspclient.htmlclient.beans.restclient;

/**
 * Exception thrown during a call to the XML Services REST server.
 *
 * @author Sergiu Indrie
 */
public class XmlServicesClientException extends Exception {

    public XmlServicesClientException(String message) {
        super(message);
    }

    public XmlServicesClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
