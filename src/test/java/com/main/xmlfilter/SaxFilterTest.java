package com.main.xmlfilter;

import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * Unit tests for SAX filter.
 *
 * @author sergiu.indrie
 */
public class SaxFilterTest {

    @Test
    public void testXmlTermsWithApple() throws IOException {
        String file = "/com/main/xmlfilter/terms.rdf.u8";
        String outputFilename = "output";
        String filter = "apple";

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = SaxFilterTest.class.getResourceAsStream(file);
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
            InputSource is = new InputSource(reader);
            outputStream = new FileOutputStream(outputFilename);

//            new SAXFilterasd().filter(is, filter, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        int a = 1;
    }

    @Test
    public void testThis() {
    }
}
