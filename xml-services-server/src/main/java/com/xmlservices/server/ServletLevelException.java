package com.xmlservices.server;

/**
 * Exception thrown during the servlet layer code execution, if such an exception is caught the servlet method must exit the method.
 *
 * @author Sergiu Indrie
 */
public class ServletLevelException extends Exception {

    public ServletLevelException() {
    }
}
