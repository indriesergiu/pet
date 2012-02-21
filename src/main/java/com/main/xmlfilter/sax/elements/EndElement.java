package com.main.xmlfilter.sax.elements;

/**
 * XML end element
 *
 * @author sergiu.indrie
 */
public class EndElement implements XMLElement {

    private String uri;
    private String localName;
    private String qName;

    public EndElement(String uri, String localName, String qName) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
    }

    public String getText() {
        StringBuilder result = new StringBuilder();
        result.append("</" + qName);
        // TODO handle uri, localName
        result.append(">");
        return result.toString();
    }

    public String getUri() {
        return uri;
    }

    public String getLocalName() {
        return localName;
    }

    public String getqName() {
        return qName;
    }

    @Override
    public String toString() {
        return getText();
    }
}
