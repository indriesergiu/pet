package com.main.xmlfilter;

import org.xml.sax.InputSource;

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
     * @param inputSource the input source of the file to be filtered
     * @param filter the string used to filter
     * @param outputStream the output stream where the filtered result will be placed
     * @throws Exception an error while parsing/filtering
     */
    public void filter(InputSource inputSource, String filter, OutputStream outputStream) throws Exception;
}
