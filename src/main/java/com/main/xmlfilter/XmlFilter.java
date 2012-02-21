package com.main.xmlfilter;

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
     * @param filter the string used to filter
     * @param outputStream the output stream where the filtered result will be placed
     * @throws Exception an error while parsing/filtering
     */
    public void filter(Reader reader, String filter, OutputStream outputStream) throws Exception;
}
