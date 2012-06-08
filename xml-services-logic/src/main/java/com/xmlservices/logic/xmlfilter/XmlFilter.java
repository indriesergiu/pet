package com.xmlservices.logic.xmlfilter;

import com.xmlservices.logic.api.search.SearchCriteria;

import java.io.OutputStream;
import java.io.Reader;

/**
 * ParserType interface for XML filtering
 *
 * @author sergiu.indrie
 */
public interface XmlFilter {

    /**
     * Filter the given file.
     *
     * @param reader the reader of the file to be filtered
     * @param searchCriteria the search criteria used to filter the xml file
     * @param outputStream the output stream where the filtered result will be placed
     * @throws Exception an error while parsing/filtering
     */
    public void filter(Reader reader, SearchCriteria searchCriteria, OutputStream outputStream) throws Exception;

    /**
     * Obtains the given page content from the XML file. Pages will be split only on element ends.
     *
     * @param reader the reader of the file whose page will be obtained
     * @param pageNumber the index of the page that will be retrieved
     * @return the page content
     */
    public String getPage(Reader reader, int pageNumber);

    /**
     * Updates the given page in the XML file with the new content.
     *
     * @param reader the reader of the file whose page will be updated
     * @param pageContent the new page content
     * @param pageNumber the updated page's index
     */
    public void updatePage(Reader reader, String pageContent, int pageNumber);
}
