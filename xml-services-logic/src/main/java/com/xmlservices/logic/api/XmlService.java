package com.xmlservices.logic.api;

import com.xmlservices.logic.api.search.SearchCriteria;

import java.io.InputStream;
import java.io.Writer;

/**
 * Provides services for XML files.
 *
 * @author Sergiu Indrie
 */
public interface XmlService {

    String getPage(int pageNumber, InputStream inputStream) throws XmlServiceException;

    void updatePage(int pageNumber, String newPageContent, InputStream inputStream, Writer writer) throws XmlServiceException;

    boolean isWellFormed(InputStream inputStream) throws XmlServiceException;

    String search(SearchCriteria searchCriteria, int pageNumber, InputStream inputStream) throws XmlServiceException;
}
