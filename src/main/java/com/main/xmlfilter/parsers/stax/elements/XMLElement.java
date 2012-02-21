package com.main.xmlfilter.parsers.stax.elements;

import java.util.Map;

/**
 * Universal XML element
 *
 * @author sergiu.indrie
 */
public class XMLElement {

    private ElementType type;
    private String prefix;
    private String localName;
    private String data;
    private Map<String, String> attributes;

    public XMLElement(ElementType type, String prefix, String localName, String data, Map<String, String> attributes) {
        this.type = type;
        this.prefix = prefix;
        this.localName = localName;
        this.data = data;
        this.attributes = attributes;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "XMLElement{" + "type=" + type + ", prefix='" + prefix + '\'' + ", localName='" + localName + '\'' + ", data='" + data + '\'' + ", attributes=" + attributes + '}';
    }
}
