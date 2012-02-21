package com.main.xmlfilter.config;

/**
 * Holds app config.
 *
 * @author sergiu.indrie
 */
public class Config {

    private static int searchDepth = 1;

    public static String ENCODING = "UTF-8";

    private static String insertionName = "XMLFilter";

    public static int getSearchDepth() {
        return searchDepth;
    }

    public static void setSearchDepth(int searchDepth) {
        Config.searchDepth = searchDepth;
    }

    public static String getInsertionName() {
        return insertionName;
    }

    public static void setInsertionName(String insertionName) {
        Config.insertionName = insertionName;
    }

    public static boolean match(String filter, String data) {
        return data.toLowerCase().contains(filter.toLowerCase());
    }
}
