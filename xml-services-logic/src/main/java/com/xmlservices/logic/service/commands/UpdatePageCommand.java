package com.xmlservices.logic.service.commands;

import com.xmlservices.logic.config.Config;

import java.io.*;

/**
 * Update page X from an XML file with given content.
 *
 * @author Sergiu Indrie
 */
public class UpdatePageCommand implements Command {

    // input fields
    private int requestedPage;
    private String newPageContent;

    private BufferedReader reader;
    private Writer writer;

    private int currentPage = 0;
    private int currentLine = 0;


    /**
     * app config
     */
    private Config config = Config.getInstance();

    public UpdatePageCommand(int requestedPage, String newPageContent, Reader reader, Writer writer) {
        this.requestedPage = requestedPage;
        this.newPageContent = newPageContent;
        this.reader = new BufferedReader(reader);
        this.writer = writer;
    }

    @Override
    public void execute() throws Exception {
        String line = reader.readLine();

        while (line != null) {

            // skip the lines of the page that needs updating
            if (currentPage != requestedPage) {
                writer.append(line + "\n");
            }
            currentLine++;

            // current page is completed
            if (currentLine >= config.getXmlPageSize()) {

                // this is the page that needs updating, add new content
                if (currentPage == requestedPage) {
                    writer.append(newPageContent);
                }

                currentLine = 0;
                currentPage++;
            }

            line = reader.readLine();
        }

        reader.close();
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        String filename = "D:\\sample.xml";

        String newPageContent =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<RDF xmlns:r=\"http://www.w3.org/TR/RDF/\" xmlns:d=\"http://purl.org/dc/elements/1.0/\" xmlns=\"http://dmoz.org/rdf/\">\n"
                + "    <new>Dog</new>\n";

        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream, Config.ENCODING);
        Writer writer = new FileWriter("d:\\updated.txt");

        UpdatePageCommand cmd = new UpdatePageCommand(0, newPageContent, reader, writer);
        cmd.execute();

        inputStream.close();
    }

}
