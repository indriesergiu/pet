package com.main.xmlfilter.parsers.stax.elements;

import java.util.Map;

/**
 * Universal XML element
 *
 * @author sergiu.indrie // todo move to upper level
 */
public class XmlElement {

    private ElementType type;
    private String prefix;
    private String localName;
    private String data;
    private String encoding;
    private String version;
    private Map<String, String> attributes;

    public XmlElement(ElementType type, String encoding, String version) {
        this.type = type;
        this.encoding = encoding;
        this.version = version;
    }

    public XmlElement(ElementType type, String prefix, String localName, String data, Map<String, String> attributes) {
        this.type = type;
        this.prefix = prefix;
        this.localName = localName;
        this.data = data;
        this.attributes = attributes;
    }

    public static XmlElement createStartDocumentElement(ElementType type, String encoding, String version) {
        return new XmlElement(type, encoding, version);
    }

    public static XmlElement createStartElement(Map<String, String> attributes, String prefix, String localName) {
        return new XmlElement(ElementType.START_ELEMENT, prefix, localName, null, attributes);
    }

    public static XmlElement createEndElement(String prefix, String localName) {
        return new XmlElement(ElementType.END_ELEMENT, prefix, localName, null, null);
    }

    public static XmlElement createDataElement(String data) {
        return new XmlElement(ElementType.DATA, null, null, data, null);
    }

     public static XmlElement createCommentElement(String commentText) {
        return new XmlElement(ElementType.COMMENT, null, null, commentText, null);
    }

    public static XmlElement createEndDocumentElement() {
        return new XmlElement(ElementType.END_DOCUMENT, null, null, null, null);
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "XmlElement{" + "type=" + type + ", prefix='" + prefix + '\'' + ", localName='" + localName + '\'' + ", data='" + data + '\'' + ", encoding='" + encoding + '\'' + ", version='"
               + version + '\'' + ", attributes=" + attributes + '}';
    }
}
