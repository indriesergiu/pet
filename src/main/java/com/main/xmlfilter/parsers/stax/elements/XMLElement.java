package com.main.xmlfilter.parsers.stax.elements;

import com.main.xmlfilter.commands.xml.XmlUtils;

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

    public String toXml() {
        StringBuilder result = new StringBuilder();
        switch (type) {
            case START_DOCUMENT:
                toXmlStartDocument(result);
                break;
            case START_ELEMENT:
                toXmlStartElement(result);
                break;
            case COMMENT:
                toXmlComment(result);
                break;
            case DATA:
                toXmlData(result);
                break;
            case END_ELEMENT:
                toXmlEndElement(result);
                break;
        }

        return result.toString();
    }

    private void toXmlStartDocument(StringBuilder result) {
        if (encoding != null || version != null) {
            result.append("<?xml");
            result.append(version != null ? " version=\"" + version + "\"" : "");
            result.append(encoding != null ? " encoding=\"" + encoding + "\"" : "");
            result.append("?>");
        }
    }

    private void toXmlEndElement(StringBuilder result) {
        result.append("</" + XmlUtils.getFullName(prefix, localName) + ">");
    }

    private void toXmlData(StringBuilder result) {
        result.append(data);
    }

    private void toXmlComment(StringBuilder result) {
        result.append("<!--" + data + "-->");
    }

    private void toXmlStartElement(StringBuilder result) {
        result.append("<" + XmlUtils.getFullName(prefix, localName));
        if (attributes != null) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                result.append(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
            }
        }
        result.append('>');
    }

    @Override
    public String toString() {
        return "XmlElement{" + "type=" + type + ", prefix='" + prefix + '\'' + ", localName='" + localName + '\'' + ", data='" + data + '\'' + ", encoding='" + encoding + '\'' + ", version='"
               + version + '\'' + ", attributes=" + attributes + '}';
    }
}
