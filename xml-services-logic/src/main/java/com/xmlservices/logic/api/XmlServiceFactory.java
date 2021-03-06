package com.xmlservices.logic.api;

/**
 * Creates {@link XmlService} objects.
 *
 * @author Sergiu Indrie
 */
public class XmlServiceFactory {

    private static XmlServiceImpl xmlServiceImpl = new XmlServiceImpl();

    /**
     * Creates the default implementation for {@link XmlService}
     *
     * @return the default implementation for {@link XmlService}
     */
    public static XmlService getXmlService() {
        return xmlServiceImpl;
    }
}
