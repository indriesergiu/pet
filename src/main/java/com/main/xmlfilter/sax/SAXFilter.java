package com.main.xmlfilter.sax;

import com.main.xmlfilter.Config;
import com.main.xmlfilter.XmlFilter;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 * SAX reader
 *
 * @author sergiu.indrie
 */
public class SAXFilter implements XmlFilter {

    public void filter(Reader reader, String filter, OutputStream outputStream) throws Exception {
        InputSource inputSource = new InputSource(reader);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputSource, new SAXFilterHandler(filter, outputStream));
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
