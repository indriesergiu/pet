package com.xmlservices.logic.parsers.stax;

import com.xmlservices.logic.XmlFilter;
import com.xmlservices.logic.api.search.SearchCriteria;
import com.xmlservices.logic.config.Config;
import com.xmlservices.logic.parsers.stax.elements.XmlElement;

import javax.xml.stream.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * StAX XML filter.
 *
 * @author sergiu.indrie
 */
public class StAXFilter implements XmlFilter {

    private SearchCriteria searchCriteria;

    /**
     * the current depth
     */
    private int depth;
    /**
     * the filtered XML's elements
     */
    private Stack<XmlElement> elements;
    /**
     * indicates that the searched element was found in the current node
     */
    private boolean found = false;
    /**
     * tells the depth of the next tag that has to be added into the element stack even if the found element is not true
     */
    private int lastFoundDepth = -1;
    /**
     * tells if the custom node has been inserted
     */
    private boolean customNodeInserted = false;

    /**
     * app config
     */
    private Config config = Config.getInstance();

    // fields for xml start document
    private String encoding;
    private String version;

    public StAXFilter() {
        elements = new Stack<XmlElement>();
    }

    @Override
    public void filter(Reader reader, SearchCriteria searchCriteria, OutputStream outputStream) throws Exception {
        this.searchCriteria = searchCriteria;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty("javax.xml.stream.isValidating", false);
        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(reader);
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Config.ENCODING);
        // try to resolve indentation problem
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(outputStreamWriter);
        //        XMLStreamWriter writer = new IndentingXMLStreamWriter(outputFactory.createXMLStreamWriter(outputStream));     // compile error
        writer = new javanet.staxutils.IndentingXMLStreamWriter(writer);
        process(xmlStreamReader, writer);
        reader.close();
        writer.flush();
        writer.close();
    }

    @Override
    public String getPage(Reader reader, int pageNumber) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updatePage(Reader reader, String pageContent, int pageNumber) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Holds the main filter logic.
     */
    private void process(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        startDocument(reader);  // the START_DOCUMENT is never reached

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    startElement(reader);
                    break;
                case XMLStreamReader.CHARACTERS:
                    characters(reader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    endElement(reader);
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    endDocument(writer);
                    break;
            }
        }
    }

    private void startDocument(XMLStreamReader reader) {
        encoding = reader.getCharacterEncodingScheme();
        version = reader.getVersion();
    }

    private void startElement(XMLStreamReader reader) {
        Map<String, String> attributes = buildAttributeMap(reader);
        String prefix = reader.getPrefix();
        String localName = reader.getLocalName();
        XmlElement item = XmlElement.createStartElement(attributes, prefix, localName);
        elements.push(item);

        // if search depth has not been reached, don't start searching
        if (depth >= config.getSearchDepth()) {
            if (attributes.size() > 0) {
                for (String attributeName : attributes.keySet()) {
                    if (searchCriteria.matchAttribute(attributes.get(attributeName))) {
                        found = true;
                        lastFoundDepth = depth;
                        break;
                    }
                }
            }
        }
        depth++;
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

    private void endElement(XMLStreamReader reader) {
        String qName = getFullName(reader.getPrefix(), reader.getLocalName());
        String prefix = reader.getPrefix();
        String localName = reader.getLocalName();
        XmlElement item = XmlElement.createEndElement(prefix, localName);
        elements.push(item);
        depth--;

        if (depth <= config.getSearchDepth() && !found) {
            // if this depth is not the expected one, remove the one
            if (depth != lastFoundDepth - 1) {
                // we are outside the search level and the filter has not been found, remove this node
                popNode(qName);
            }
        } else if (depth <= config.getSearchDepth() && found) {
            // the search level has been reached and a filter was found, leave the elements in the stack and continue search
            found = false;

            // add the custom node
            if (!customNodeInserted) {
                pushCustomNode();
                customNodeInserted = true;
            }
        }

        // if the depth is the expected one, change it accordingly
        if (depth == lastFoundDepth - 1) {
            lastFoundDepth--;
        }
    }

    private void characters(XMLStreamReader reader) {
        String data = reader.getText();
        if (data.contains("\n")) {
            return;
        }

        if (searchCriteria.matchData(data)) {
            found = true;
            lastFoundDepth = depth;
        }
        XmlElement item = XmlElement.createDataElement(data);
        elements.push(item);
    }

    private void endDocument(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument(encoding, version);
        for (XmlElement element : elements) {
            switch (element.getType()) {
                case START_ELEMENT:
                    writer.writeStartElement(element.getPrefix() != null ? element.getPrefix() : "", element.getLocalName(), "");
                    Map<String, String> attributes = element.getAttributes();
                    if (attributes != null && attributes.size() > 0) {
                        for (String attribute : attributes.keySet()) {
                            writer.writeAttribute(attribute, attributes.get(attribute));
                        }
                    }
                    break;
                case DATA:
                    writer.writeCharacters(element.getData());
                    break;
                case END_ELEMENT:
                    writer.writeEndElement();
                    break;
            }
        }
    }

    private void popNode(String qName) {
        boolean nodeFound;
        // remove first node with identical name
        do {
            elements.pop();
            XmlElement element = elements.peek();
            nodeFound = qName.equals(getFullName(element.getPrefix(), element.getLocalName()));
        } while (!nodeFound);

        elements.pop();
    }

    private void pushCustomNode() {
        String name = config.getInsertionName();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(name + "Attr", name + "Value");
        elements.push(XmlElement.createStartElement(attributes, null, name));
        elements.push(XmlElement.createStartElement(null, null, name + "Child"));
        elements.push(XmlElement.createDataElement(name + "Data"));
        elements.push(XmlElement.createEndElement(null, name + "Child"));
        elements.push(XmlElement.createEndElement(null, name));
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

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        String filter = "apple";
        String outputFile = "D:\\output.xml";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        new StAXFilter().filter(reader, SearchCriteria.createSearchCriteriaFromValue(filter), outputStream);
        inputStream.close();
        outputStream.close();
    }
}
