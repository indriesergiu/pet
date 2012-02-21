package com.main.xmlfilter.parsers.sax;

import com.main.xmlfilter.XmlFilter;
import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.parsers.sax.elements.DataElement;
import com.main.xmlfilter.parsers.sax.elements.EndElement;
import com.main.xmlfilter.parsers.sax.elements.StartElement;
import com.main.xmlfilter.parsers.sax.elements.XMLElement;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * SAX handler
 *
 * @author sergiu.indrie
 */
public class SAXFilter extends DefaultHandler implements XmlFilter {

    /**
     * the filter
     */
    private String filter;
    /**
     * output file's stream
     */
    private OutputStream outputStream;
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
     * the XML writer
     */
    private XMLWriter writer;
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

    public SAXFilter() {
        elements = new Stack<XMLElement>();
        writer = new XMLWriter();
    }

    public void filter(Reader reader, String filter, OutputStream outputStream) throws Exception {
        InputSource inputSource = new InputSource(reader);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        this.filter = filter;
        this.outputStream = outputStream;
        saxParser.parse(inputSource, this);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elements.push(new StartElement(uri, localName, qName, buildAttributeMap(attributes)));

        // if search depth has not been reached, don't start searching
        if (depth >= config.getSearchDepth()) {
            if (config.match(filter, qName)) {
                found = true;
                lastFoundDepth = depth;
            } else if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (config.match(filter, attributes.getValue(i)) || config.match(filter, attributes.getQName(i))) {
                        found = true;
                        lastFoundDepth = depth;
                        break;
                    }
                }
            }
        }
        depth++;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elements.push(new EndElement(uri, localName, qName));
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

    private void popNode(String qName) {
        elements.pop();     // remove first node with identical name
        while (!qName.equals(elements.peek().getqName())) {
            elements.pop();
        }
        elements.pop();
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String data = new String(ch, start, length);

        if (data.contains("\n")) {
            return;
        }

        if (config.match(filter, data)) {
            found = true;
            lastFoundDepth = depth;
        }
        elements.push(new DataElement(data));
    }

    public void endDocument() {
        writer.write(elements.iterator(), outputStream);
    }

    private Map<String, String> buildAttributeMap(Attributes attributes) {
        if (attributes != null && attributes.getLength() > 0) {
            Map<String, String> result = new HashMap<String, String>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                result.put(attributes.getQName(i), attributes.getValue(i));
            }
            return result;
        } else {
            return null;
        }
    }

    private void pushCustomNode() {
        String name = config.getInsertionName();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(name + "Attr", name + "Value");
        elements.push(new StartElement(null, null, name, attributes));
        elements.push(new StartElement(null, null, name + "Child", null));
        elements.push(new DataElement(name + "Data"));
        elements.push(new EndElement(null, null, name + "Child"));
        elements.push(new EndElement(null, null, name));
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        String filter = "apple";
        String outputFile = "D:\\output.xml";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        new SAXFilter().filter(reader, filter, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
