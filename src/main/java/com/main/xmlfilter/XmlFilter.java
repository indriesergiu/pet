package com.main.xmlfilter;

import com.main.xmlfilter.sax.SAXFilter;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Main class for running the XML filter app.
 *
 * @author sergiu.indrie
 */
public class XmlFilter {

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0 || args[0] == null || args[0].equals("")) {
            throw new IllegalArgumentException("No input file specified.");
        }
        if (args.length < 2 || args[1] == null || args[1].equals("")) {
            throw new IllegalArgumentException("No filter specified.");
        }
        // TODO obtain output filename
        //        String filename = "/com/main/xmlfilter/terms.rdf.u8.gz";
        String filename = args[0];
        String filter = args[1];
        String outputFilename = args[2];
        System.out.println("Processing file: " + filename);

        //        InputStream inputStream = SAXFilter.class.getResourceAsStream(filename);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            outputStream = new FileOutputStream(outputFilename);

            if (filename.endsWith(".xml")) {
                new SAXFilter().filter(inputStream, filter, outputStream);
            } else if (filename.endsWith(".gz")) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                new SAXFilter().filter(gzipInputStream, filter, outputStream);
                gzipInputStream.close();
            } else {
                new SAXFilter().filter(inputStream, filter, outputStream);
            }

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
    }
}
