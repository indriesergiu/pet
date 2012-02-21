package com.main.xmlfilter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/**
 * Main class for running the XML filter app.
 *
 * @author sergiu.indrie
 */
public class XmlFilterMain {

    public static final int TWO_HOURS = 2 * 60 * 60 * 1000;
    public static final String ALL_PARSERS = "all";

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
        String inputNodeName = args[3];
        String parser = args[4];

        if (parser.equals(ALL_PARSERS)) {
            for (ParserType parserType : ParserType.values()) {
                log("Using parser: " + parserType);
                XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
                runFilter(filename, filter, outputFilename, inputNodeName, xmlFilter);
                log("\n\n");
            }
        } else {
            ParserType parserType = ParserType.valueOf(parser.toUpperCase());
            XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
            log("Using parser: " + parserType);
            runFilter(filename, filter, outputFilename, inputNodeName, xmlFilter);
        }
    }

    private static void runFilter(String filename, String filter, String outputFilename, String inputNodeName, XmlFilter xmlFilter) throws IOException {
        Config.setInsertionName(inputNodeName);
        log("Processing file: " + filename);

        Date start = new Date();
        log("Start time: " + start);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
            outputStream = new FileOutputStream(outputFilename);

            if (filename.endsWith(".xml")) {
                xmlFilter.filter(reader, filter, outputStream);
            } else if (filename.endsWith(".gz")) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                reader = new InputStreamReader(inputStream, Config.ENCODING);
                xmlFilter.filter(reader, filter, outputStream);
                gzipInputStream.close();
            } else {
                xmlFilter.filter(reader, filter, outputStream);
            }

            reader.close();
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

        Date end = new Date();
        log("End time: " + end);

        long millis = end.getTime() - start.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        log("Total execution time: " + format.format(new Date(millis - TWO_HOURS)));
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
