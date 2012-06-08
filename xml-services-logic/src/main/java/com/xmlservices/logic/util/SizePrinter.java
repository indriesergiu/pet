package com.xmlservices.logic.util;

import java.text.DecimalFormat;

/**
 * Pretty prints sizes (in bytes).
 *
 * @author sergiu.indrie
 */
public class SizePrinter {

    private static final double BASE = 1024;
    private static final double KB = BASE;
    private static final double MB = KB * BASE;
    private static final double GB = MB * BASE;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public static String formatSize(double size) {
        if (size >= GB) {
            return df.format(size / GB) + " GB";
        }
        if (size >= MB) {
            return df.format(size / MB) + " MB";
        }
        if (size >= KB) {
            return df.format(size / KB) + " KB";
        }
        return "" + (int) size + " bytes";
    }

}
