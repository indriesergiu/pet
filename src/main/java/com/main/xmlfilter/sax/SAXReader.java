package com.main.xmlfilter.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * SAX reader
 *
 * @author sergiu.indrie
 */
public class SAXReader {

    public void parse(InputStream inputStream) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputStream, new SAXHandler());
    }

    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
        //        String filename = "../terms.rdf.u8.gz";
        String filename = "sample.xml";

        if (filename.endsWith(".xml")) {
            new SAXReader().parse(SAXReader.class.getResourceAsStream(filename));
        } else if (filename.endsWith(".gz")) {
            new SAXReader().parse(new GZIPInputStream(SAXReader.class.getResourceAsStream(filename)));
        } else {
            new SAXReader().parse(SAXReader.class.getResourceAsStream(filename));
        }

    }
}
