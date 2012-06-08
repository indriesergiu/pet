package com.xmlservices.logic.api.commands;

import com.xmlservices.logic.config.Config;

import java.io.*;

/**
 * Obtain page number X from a file.
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
        String line = reader.readLine();

        while (line != null) {
            if (currentPage == requestedPage) {
                buffer.append(line + "\n");
            }
            currentLine++;

            // current page is completed
            if (currentLine >= config.getXmlPageSize()) {

                // page has been found, quit reading
                if (currentPage == requestedPage) {
                    break;
                }

                currentLine = 0;
                currentPage++;
            }

            line = reader.readLine();
        }

        reader.close();
    }

    public String getRequestedPageContent() {
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";
        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);

        GetPageCommand cmd = new GetPageCommand(1, reader);
        cmd.execute();

        System.out.println(cmd.getRequestedPageContent());

        inputStream.close();
    }
}
