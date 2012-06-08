package com.xmlservices.logic.service.commands.xml;

import com.xmlservices.logic.parsers.stax.elements.ElementType;
import com.xmlservices.logic.parsers.stax.elements.XmlElement;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class StAXGenericParser implements CommandParser {

    private Reader reader;
    private XMLStreamReader xmlStreamReader;
    private boolean firstTime = false;
    private boolean finishedParsing = false;

    public StAXGenericParser(Reader reader) throws XMLStreamException {
        this.reader = reader;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty("javax.xml.stream.isValidating", false);
        xmlStreamReader = inputFactory.createXMLStreamReader(reader);
    }

    public XmlElement getNextElement() throws Exception {
        if (finishedParsing) {
            return null;
        }

        XmlElement element = null;

        // TODO try switch without calling next from the first time
        if (!firstTime) {
            firstTime = true;
            return startDocument(xmlStreamReader);  // the START_DOCUMENT is never reached
        }

        // iterate until a needed element is found
        do {
            int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    element = startElement(xmlStreamReader);
                    break;
                case XMLStreamReader.CHARACTERS:
                    element = characters(xmlStreamReader);
                    break;
                case XMLStreamReader.COMMENT:
                    element = comment(xmlStreamReader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    element = endElement(xmlStreamReader);
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    element = endDocument();
                    finishedParsing = true;
                    break;
            }
        } while (element == null && xmlStreamReader.hasNext());

        if (!xmlStreamReader.hasNext()) {
            xmlStreamReader.close();
            reader.close();
        }

        return element;
    }

    public void close() {
        try {
            xmlStreamReader.close();
            reader.close();
        } catch (Exception e) {
            // todo log me
        }
    }

    private XmlElement comment(XMLStreamReader xmlStreamReader) {
        return XmlElement.createCommentElement(xmlStreamReader.getText());
    }

    private XmlElement startDocument(XMLStreamReader reader) {
        return XmlElement.createStartDocumentElement(ElementType.START_DOCUMENT, reader.getCharacterEncodingScheme(), reader.getVersion());
    }

    private XmlElement startElement(XMLStreamReader reader) {
        Map<String, String> attributes = buildAttributeMap(reader);
        String prefix = reader.getPrefix();
        String localName = reader.getLocalName();
        XmlElement item = XmlElement.createStartElement(attributes, prefix, localName);
        return item;
    }

    private Map<String, String> buildAttributeMap(XMLStreamReader reader) {
        Map<String, String> result = new HashMap<String, String>(reader.getAttributeCount());
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            result.put(getFullName(reader.getAttributePrefix(i), reader.getAttributeLocalName(i)), reader.getAttributeValue(i));
        }
        int namespaceCount = reader.getNamespaceCount();
        if (namespaceCount > 0) {
            for (int i = 0; i < namespaceCount; i++) {
                result.put(getFullName("xmlns", reader.getNamespacePrefix(i)), reader.getNamespaceURI(i));
            }
        }

        return result;
    }

    private XmlElement endElement(XMLStreamReader reader) {
        String prefix = reader.getPrefix();
        String localName = reader.getLocalName();
        XmlElement item = XmlElement.createEndElement(prefix, localName);
        return item;
    }

    private XmlElement characters(XMLStreamReader reader) {
        String data = reader.getText();
        XmlElement item = XmlElement.createDataElement(data);
        return item;
    }

    private XmlElement endDocument() throws XMLStreamException {
        return XmlElement.createEndDocumentElement();
    }

    private String getFullName(String prefix, String local) {
        StringBuilder qName = new StringBuilder();
        qName.append(prefix != null ? prefix : "");
        if (prefix != null && !prefix.equals("") && local != null && !local.equals("")) {
            qName.append(":");
        }
        qName.append(local != null ? local : "");
        return qName.toString();
    }
}
