package com.main.xmlfilter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Parser interface for XML filtering
 *
 * @author sergiu.indrie
 */
public interface XmlFilterParser {

    /**
     * Filter the given file.
     *
     * @param inputStream the input stream of the file to be filtered
     * @param filter the string used to filter
     * @param outputStream the output stream where the filtered result will be placed
     * @throws Exception an error while parsing/filtering
     */
    public void filter(InputStream inputStream, String filter, OutputStream outputStream) throws Exception;
}
