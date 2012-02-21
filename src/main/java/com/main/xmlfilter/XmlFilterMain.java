package com.main.xmlfilter;

import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.monitor.MemoryTracker;
import com.main.xmlfilter.util.SizePrinter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Main class for running the XML filter app.
 *
 * @author sergiu.indrie
 */
public class XmlFilterMain {

    public static final int TWO_HOURS = 2 * 60 * 60 * 1000;
    public static final String ALL_PARSERS = "all";
    protected static final String GZIP_EXTENSION = ".gz";

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0 || args[0] == null || args[0].equals("")) {
            throw new IllegalArgumentException("No input file specified.");
        }
        if (args.length < 2 || args[1] == null || args[1].equals("")) {
            throw new IllegalArgumentException("No filter specified.");
        }

        String filename = args[0];
        String filter = args[1];
        String outputFilename = args[2];
        String inputNodeName = args[3];
        String parser = args[4];
        String gzip = args[5];

        if (parser.equals(ALL_PARSERS)) {
            for (ParserType parserType : ParserType.values()) {
                log("Using parser: " + parserType);
                XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
                runFilter(filename, filter, outputFilename + "_" + parserType, inputNodeName, xmlFilter, Boolean.valueOf(gzip));
                log("\n\n");
            }
        } else {
            ParserType parserType = ParserType.valueOf(parser.toUpperCase());
            XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
            log("Using parser: " + parserType);
            runFilter(filename, filter, outputFilename, inputNodeName, xmlFilter, Boolean.valueOf(gzip));
        }

        MemoryTracker.getInstance().shutdown();
    }

    private static void runFilter(String filename, String filter, String outputFilename, String inputNodeName, XmlFilter xmlFilter, Boolean gzip) throws IOException {
        MemoryTracker.getInstance().startTracking();

        Config.getInstance().setInsertionName(inputNodeName);
        log("Processing file: " + filename);

        Date start = new Date();
        log("Start time: " + start);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
            if (gzip) {
                outputFilename = outputFilename + GZIP_EXTENSION;
                outputStream = new FileOutputStream(outputFilename);
                outputStream = new GZIPOutputStream(outputStream);
            } else {
                outputStream = new FileOutputStream(outputFilename);
            }

            if (filename.endsWith(".xml")) {
                xmlFilter.filter(reader, filter, outputStream);
            } else if (filename.endsWith(GZIP_EXTENSION)) {
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
                if (gzip && outputStream instanceof GZIPOutputStream) {
                    ((GZIPOutputStream) outputStream).finish();
                }
                outputStream.close();
            }
        }

        Date end = new Date();
        log("End time: " + end);

        log("Output file: " + outputFilename);

        long millis = end.getTime() - start.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        log("Total execution time: " + format.format(new Date(millis - TWO_HOURS)));

        MemoryTracker.getInstance().stopTracking();
        log("Maximum memory usage: " + SizePrinter.formatSize(MemoryTracker.getInstance().getMaxUsage()));
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
