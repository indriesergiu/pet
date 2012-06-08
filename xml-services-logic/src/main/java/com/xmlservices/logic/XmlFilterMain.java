package com.xmlservices.logic;

import com.xmlservices.logic.api.search.SearchCriteria;
import com.xmlservices.logic.config.Config;
import com.xmlservices.logic.monitor.MemoryTracker;
import com.xmlservices.logic.monitor.MemoryTrackerFactory;
import com.xmlservices.logic.monitor.MemoryTrackerType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    public static void main(String[] args) {
        log("Input parameters: " + Arrays.asList(args).toString() + "\n");

        String filename = args[0];
        String filter = args[1];
        String outputFilename = args[2];
        String inputNodeName = args[3];
        String parser = args[4];
        String gzip = args[5];
        String memoryTrackerType = args[6];
        Boolean showUsage = Boolean.valueOf(args[7]);

        if (showUsage) {
            log(Config.getInstance().USAGE);
            return;
        }

        MemoryTracker memoryTracker = MemoryTrackerFactory.getMemoryTracker(MemoryTrackerType.valueOf(memoryTrackerType.toUpperCase()));

        if (parser.equals(ALL_PARSERS)) {
            for (ParserType parserType : ParserType.values()) {
                log("Using parser: " + parserType);
                XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
                runFilter(filename, filter, outputFilename + "_" + parserType, inputNodeName, xmlFilter, Boolean.valueOf(gzip), memoryTracker);
                log("\n\n");
            }
        } else {
            ParserType parserType = ParserType.valueOf(parser.toUpperCase());
            XmlFilter xmlFilter = XmlFilterFactory.getFilter(parserType);
            log("Using parser: " + parserType);
            runFilter(filename, filter, outputFilename, inputNodeName, xmlFilter, Boolean.valueOf(gzip), memoryTracker);
        }

        // shutdown the scheduler service
        Config.getInstance().getScheduler().shutdown();
    }

    private static void runFilter(String filename, String filter, String outputFilename, String inputNodeName, XmlFilter xmlFilter, Boolean gzip, MemoryTracker memoryTracker) {
        try {
            memoryTracker.startTracking();

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

                SearchCriteria searchCriteria = SearchCriteria.createSearchCriteriaFromValue(filter);

                if (filename.endsWith(".xml")) {
                    xmlFilter.filter(reader, searchCriteria, outputStream);
                } else if (filename.endsWith(GZIP_EXTENSION)) {
                    GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                    reader = new InputStreamReader(gzipInputStream, Config.ENCODING);
                    xmlFilter.filter(reader, searchCriteria, outputStream);
                    gzipInputStream.close();
                } else {
                    xmlFilter.filter(reader, searchCriteria, outputStream);
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

            memoryTracker.stopTracking();
            log("Maximum used memory: " + memoryTracker.getMaxUsage());
        } catch (Exception e) {
            e.printStackTrace();
            log("Execution failed.");
        }
    }

    private static void log(String s) {
        //        Config.getInstance().getLogger().log(Level.INFO, s);
        System.out.println(s);
    }
}
