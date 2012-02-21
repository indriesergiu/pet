package com.main.xmlfilter.sax;

import com.main.xmlfilter.Config;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.OutputStream;
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

    public SAXFilterHandler(String filter, OutputStream outputStream) {
        this.filter = filter;
        this.outputStream = outputStream;
        elements = new Stack<XMLElement>();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("Start Element :" + qName);

        // if search depth has not been reached, don't start searching
        if (depth >= Config.getSearchDepth()) {
            if (qName.contains(filter)) {
                found = true;
            } else if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getValue(i).contains(filter)) {
                        found = true;
                        break;
                    }
                }
            }
            elements.add(new XMLElement(uri, localName, qName, attributes, true));
        }

        depth++;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("End Element :" + qName);


        depth--;

        if (depth <= Config.getSearchDepth() && !found) {
            elements.clear();
        }
        elements.add(new XMLElement(uri, localName, qName, null, false));
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        String data = new String(ch, start, length);
        System.out.println(data);

        elements.add(new XMLElement(null, null, data, null, false));
        // check string
    }
}
