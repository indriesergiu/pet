package com.main.xmlfilter;

import com.main.xmlfilter.parsers.dom.DOMFilter;
import com.main.xmlfilter.parsers.sax.SAXFilter;
import com.main.xmlfilter.parsers.stax.StAXFilter;

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
            case STAX:
                return new StAXFilter();
            case DOM:
                return new DOMFilter();
            default:
                throw new IllegalArgumentException("No such parser");
        }
    }
}
