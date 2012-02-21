package com.main.xmlfilter.sax.elements;

import java.util.Map;

/**
 * XML Start element
 *
 * @author sergiu.indrie
 */
public class StartElement implements XMLElement {

    private String uri;
    private String localName;
    private String qName;
    private Map<String, String> attributes;

    public StartElement(String uri, String localName, String qName, Map<String, String> attributes) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        this.attributes = attributes;
    }

    public String getText() {
        StringBuilder result = new StringBuilder();
        result.append("<" + qName);
        // TODO handle uri, localName
        if (attributes != null && attributes.size() > 0) {
            for (String name : attributes.keySet()) {
                result.append(" " + name + "=\"" + attributes.get(name) + "\"");
            }
        }
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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return getText();
    }
}
