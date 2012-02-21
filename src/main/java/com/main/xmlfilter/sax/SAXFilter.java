package com.main.xmlfilter.sax;

import com.main.xmlfilter.XmlFilterParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SAX reader
 *
 * @author sergiu.indrie
 */
public class SAXFilter implements XmlFilterParser {

    public void filter(InputStream inputStream, String filter, OutputStream outputStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputStream, new SAXFilterHandler(filter, outputStream));
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        String filter = "Dog";
        String outputFile = "D:\\output.xml";
        InputStream inputStream = new FileInputStream(filename);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        new SAXFilter().filter(inputStream, filter, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
