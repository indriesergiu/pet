package com.main.xmlfilter.parsers.sax.elements;

/**
 * XML data element
 *
 * @author sergiu.indrie
 */
public class DataElement implements XMLElement {

    private String data;

    public DataElement(String data) {
        this.data = data;
    }

    public String getText() {
        return data;
    }

    public String getqName() {
        return null;
    }

    @Override
    public String toString() {
        return data;
    }
}
