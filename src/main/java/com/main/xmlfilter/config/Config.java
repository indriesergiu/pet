package com.main.xmlfilter.config;

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
}
