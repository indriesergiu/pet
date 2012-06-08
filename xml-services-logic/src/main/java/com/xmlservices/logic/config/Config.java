package com.xmlservices.logic.config;

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

    // number of lines in a XML page
    private int xmlPageSize = 25;

    public String USAGE = "XML Filter\n"
                          + "\n"
                          + "To run the app with default settings type\n"
                          + "\n"
                          + "ant\n"
                          + "\n"
                          + "The command line arguments of the app are:\n\n"
                          + "\tfile\t\t- the XML input file that will be filtered (this can be \".xml\" or \".gz\")\n"
                          + "\t\t\t  default value is \"d:/sample.xml\"\n"
                          + "\tfilter\t\t- the string filter that will be searched in the XML file\n"
                          + "\t\t\t  default value is \"apple\"\n"
                          + "\toutputFile\t- the output file name that will contain the filtered result\n"
                          + "\t\t\t  default value is \"d:/out.xml\"\n"
                          + "\tinputNodeName\t- the name that will be used to create the custom node that will be inserted in the resulting file\n"
                          + "\t\t\t  default value is \"Lala\"\n"
                          + "\tparser\t\t- the type of XML parser that will be used to process the file \n"
                          + "\t\t\t  possible values are: sax, dom, stax, all\n"
                          + "\t\t\t  default value is \"dom\"\n"
                          + "\tgzip\t\t- this indicates if the result is to be compressed in gzip format, possible values are: true, false\n"
                          + "\t\t\t  default value is \"false\"\n"
                          + "\tmemoryTracker\t- the type of memory usage tracker that will be used, possible values: runtime, mx\n"
                          + "\t\t\t  default value is \"runtime\"\n"
                          + "\thelp\t\t- generates this message, possible values: true, false\n"
                          + "\t\t\t  default value is \"false\"\n"
                          + "\nHere's a sample command with all the arguments\n"
                          + "\n"
                          + "ant -Dfile=\"d:/sample.gz\" -Dfilter=\"apple\" -DoutputFile=\"d:\\o.txt\" -DinputNodeName=\"Lala\" -Dparser=\"all\" -Dgzip=\"true\" -DmemoryTracker=\"runtime\"\n";

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

    public int getXmlPageSize() {
        return xmlPageSize;
    }

    public void setXmlPageSize(int xmlPageSize) {
        this.xmlPageSize = xmlPageSize;
    }
}
