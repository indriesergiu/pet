package com.main.xmlfilter.stax;

import com.main.xmlfilter.Config;
import com.main.xmlfilter.XmlFilter;
import com.main.xmlfilter.sax.elements.StartElement;
import com.main.xmlfilter.sax.elements.XMLElement;

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
public class StaxFilter implements XmlFilter {

    private String filter;

    /**
     * the current depth
     */
    private int depth;
    /**
     * the filtered XML's elements
     */
    private Stack<XMLElement> elements;
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

    public void filter(Reader reader, String filter, OutputStream outputStream) throws Exception {
        this.filter = filter;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty("javax.xml.stream.isValidating", false);
        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(reader);
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(outputStream);
        process(xmlStreamReader, writer);
        reader.close();
        writer.flush();
        writer.close();
    }

    /**
     * Holds the main filter logic.
     */
    private void process(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    startElement(reader);
                    break;
                case XMLStreamReader.CHARACTERS:
                    characters();
                case XMLStreamReader.END_ELEMENT:
                    endElement();
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    endDocument();
                    break;
            }
        }
    }

    private void startElement(XMLStreamReader reader) {
        String fullName = getFullName(reader.getPrefix(), reader.getLocalName());
        Map<String, String> attributes = buildAttributeMap(reader);
        elements.push(new StartElement(null, null, fullName, attributes));

        // if search depth has not been reached, don't start searching
        if (depth >= Config.getSearchDepth()) {
            if (Config.match(filter, fullName)) {
                found = true;
                lastFoundDepth = depth;
            } else if (attributes.size() > 0) {
                for (String attributeName : attributes.keySet()) {
                    if (Config.match(filter, attributes.get(attributeName)) || Config.match(filter, attributeName)) {
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
            result.put(getFullName(reader.getAttributePrefix(i), reader.getAttributeValue(i)), reader.getAttributeValue(i));
        }
        return result;
    }

    private void endElement() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void characters() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void endDocument() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private String getFullName(String prefix, String local) {
        StringBuilder qName = new StringBuilder();
        qName.append(prefix != null ? prefix + ":" : "");
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
        new StaxFilter().filter(reader, filter, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
