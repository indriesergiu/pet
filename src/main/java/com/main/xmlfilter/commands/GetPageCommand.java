package com.main.xmlfilter.commands;

import com.main.xmlfilter.config.Config;

import java.io.*;

/**
 * Obtain page number X from the XML file.
 *
 * @author Sergiu Indrie
 */
public class GetPageCommand implements Command {

    private int currentPage = 0;
    private int currentLine = 0;

    private int requestedPage;
    private BufferedReader reader;

    private StringBuilder buffer;

    /**
     * app config
     */
    private Config config = Config.getInstance();

    public GetPageCommand(int requestedPage, Reader reader) {
        this.requestedPage = requestedPage;
        this.reader = new BufferedReader(reader);
        buffer = new StringBuilder();
    }

    @Override
    public void execute() throws Exception {
        String line;
        do {
            line = reader.readLine();
            if (currentPage == requestedPage) {
                buffer.append(line + "\n");
            }
            currentLine++;

            // current page is completed
            if (currentLine >= config.getXmlPageSize()) {

                // page has been found, quit reading
                if (currentPage == requestedPage) {
                    reader.close();
                    break;
                }

                currentLine = 0;
                currentPage++;
            }

        } while (line != null);
    }

    public String getRequestedPageContent() {
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

        GetPageCommand cmd = new GetPageCommand(0, reader);
        cmd.execute();

        System.out.println(cmd.getRequestedPageContent());

        inputStream.close();
    }
}
