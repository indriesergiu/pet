package com.main.xmlfilter;

/**
 * Holds app config.
 *
 * @author sergiu.indrie
 */
public class Config {

    private static int searchDepth = 1;

    public static String ENCODING = "UTF-8";

    public static int getSearchDepth() {
        return searchDepth;
    }

    public static void setSearchDepth(int searchDepth) {
        Config.searchDepth = searchDepth;
    }
}
