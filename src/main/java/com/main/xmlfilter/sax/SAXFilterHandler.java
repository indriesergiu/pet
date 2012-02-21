package com.main.xmlfilter.sax;

import com.main.xmlfilter.Config;
import com.main.xmlfilter.sax.elements.DataElement;
import com.main.xmlfilter.sax.elements.EndElement;
import com.main.xmlfilter.sax.elements.StartElement;
import com.main.xmlfilter.sax.elements.XMLElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * SAX handler
 *
 * @author sergiu.indrie
 */
public class SAXFilterHandler extends DefaultHandler {

    private String filter;
    private OutputStream outputStream;
    private int depth;
    private Stack<XMLElement> elements;
    private boolean found = false;
    private XMLWriter writer;

    public SAXFilterHandler(String filter, OutputStream outputStream) {
        this.filter = filter;
        this.outputStream = outputStream;
        elements = new Stack<XMLElement>();
        writer = new XMLWriter();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elements.push(new StartElement(uri, localName, qName, buildAttributeMap(attributes)));

        // if search depth has not been reached, don't start searching
        if (depth >= Config.getSearchDepth()) {
            if (match(qName)) {
                found = true;
            } else if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (match(attributes.getValue(i))) {
                        found = true;
                        break;
                    }
                }
            }
        }
        depth++;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        elements.push(new EndElement(uri, localName, qName));
        depth--;

        if (depth <= Config.getSearchDepth() && !found) {
            // the search level has been reached and no filter was found, remove this node from the stack
            elements.pop();
            while (!elements.empty() && !qName.equals(elements.pop().getqName())) {
                ;
            }
        } else if (depth <= Config.getSearchDepth() && found) {
            // the search level has been reached and a filter was found, send the elements to the writer
            writer.write(elements, outputStream);
            elements.clear();
            found = false;
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        String data = new String(ch, start, length);

        if (data.contains("\n")) {
            return;
        }

        if (match(data)) {
            found = true;
        }
        elements.push(new DataElement(data));
    }

    private boolean match(String data) {
        return data.toLowerCase().contains(filter.toLowerCase());
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
}
