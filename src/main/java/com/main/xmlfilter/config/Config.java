package com.main.xmlfilter.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Holds app config.
 *
 * @author sergiu.indrie
 */
public class Config {

    private static Config INSTANCE = new Config();

    private int searchDepth = 1;

    public static String ENCODING = "UTF-8";

    private String insertionName = "XMLFilter";

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();

    private Config() {
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public String getInsertionName() {
        return insertionName;
    }

    public void setInsertionName(String insertionName) {
        this.insertionName = insertionName;
    }

    public boolean match(String filter, String data) {
        return data.toLowerCase().contains(filter.toLowerCase());
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public java.util.logging.Logger getLogger() {
        return logger;
    }
}
