package com.main.xmlfilter;

import com.main.xmlfilter.sax.SAXFilter;

/**
 * Creates XML filters.
 *
 * @author sergiu.indrie
 */
public class XmlFilterFactory {

    /**
     * Creates an XML filter based on the given parserType.
     *
     * @param type the parser type
     * @return an XML filter
     */
    public static XmlFilter getFilter(ParserType type) {
        switch (type) {
            case SAX:
                return new SAXFilter();
            default:
                throw new IllegalArgumentException("No such parser");
        }
    }
}
