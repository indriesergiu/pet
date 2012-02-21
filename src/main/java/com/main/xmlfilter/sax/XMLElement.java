package com.main.xmlfilter.sax;

import org.xml.sax.Attributes;

/**
 * An XML element
 *
 * @author sergiu.indrie
 */
public class XMLElement {

    private String uri;
    private String localName;
    private String qName;
    private Attributes attributes;
    // TODO add enum for START/END/DATA (perhaps even Interface with getString)
    private boolean startElement;

    public XMLElement(String uri, String localName, String qName, Attributes attributes, boolean startElement) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        this.attributes = attributes;
        this.startElement = startElement;
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

    public Attributes getAttributes() {
        return attributes;
    }

    public boolean isStartElement() {
        return startElement;
    }
}
