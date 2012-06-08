package com.xmlservices.consoleclient;

import com.xmlservices.consoleclient.client.HttpClient;
import com.xmlservices.consoleclient.client.HttpClientException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Command line for running the {@link HttpClient} commands.
 *
 * @author Sergiu Indrie
 */
public class HttpClientConsole {

    private Logger logger = Logger.getLogger(HttpClientConsole.class);

    private HttpClient httpClient;

    /**
     * for i18n
     */
    private ResourceBundle messages;

    private String prompt = "> ";
    // commands
    public static final String QUIT[] = new String[]{"quit", "q"};
    public static final String LOGIN[] = new String[]{"login", "l"};
    public static final String VIEW[] = new String[]{"view", "v"};
    public static final String SEARCH[] = new String[]{"search", "s"};
    public static final String UPDATE[] = new String[]{"update", "u"};

    public HttpClientConsole() throws IOException {
        httpClient = new HttpClient();
        init();
    }

    private void init() throws IOException {
        logger.debug("Starting configuration loading.");
        InputStream resourceAsStream = getClass().getResourceAsStream("/console.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        resourceAsStream.close();
        logger.debug("Finished configuration loading.");

        // set properties
        Locale locale;
        try {
            if (Boolean.parseBoolean(properties.getProperty("console.locale.overwrite.system"))) {
                locale = new Locale(properties.getProperty("console.locale.language"), properties.getProperty("console.locale.country"));
            } else {
                locale = Locale.getDefault();
            }
        } catch (Exception e) {
            locale = Locale.getDefault();
        }

        messages = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public void run() throws IOException {

        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String entireCommand;
        boolean quit = false;
        do {
            printToConsoleInline(prompt);
            entireCommand = in.readLine();

            String command = entireCommand.split(" ")[0];

            try {
                if (matchesCommand(command, LOGIN)) {
                    callLogin(entireCommand);
                } else if (matchesCommand(command, VIEW)) {
                    callView(entireCommand);
                } else if (matchesCommand(command, SEARCH)) {
                    callSearch(entireCommand);
                } else if (matchesCommand(command, UPDATE)) {
                    callUpdate(entireCommand);
                } else {
                    quit = matchesCommand(command, QUIT);
                    if (!quit) {
                        handleUnknownCommand(command);
                    }
                }
            } catch (HttpClientException e) {
                logger.error("An error has occurred during the call to HttpClient.", e);
                printToConsole(messages.getString("http.client.error"));
            }
        } while (!quit);
    }

    private void callUpdate(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 3) {
            logger.info("Invalid number of elements in update command: " + elements.length);
            printToConsole(MessageFormat.format(messages.getString("error.validation.command.update"), elements.length));
        } else {
            boolean searchResponse = httpClient.update(elements[1], elements[2]);
            if (searchResponse) {
                printToConsole(messages.getString("success.command.update"));
            } else {
                printToConsole(messages.getString("failure.command.update"));
            }
        }
    }

    private void callSearch(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 3) {
            logger.info("Invalid number of elements in search command: " + elements.length);
            printToConsole(MessageFormat.format(messages.getString("error.validation.command.search"), elements.length));
        } else {
            String searchResponse = httpClient.search(elements[1], elements[2]);
            if (!searchResponse.trim().isEmpty()) {
                printToConsoleInline(searchResponse);
            }
        }
    }

    private void callView(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 2) {
            logger.info("Invalid number of elements in view command: " + elements.length);
            printToConsole(MessageFormat.format(messages.getString("error.validation.command.view"), elements.length));
        } else {
            String viewResponse = httpClient.view(elements[1]);
            if (!viewResponse.trim().isEmpty()) {
                printToConsoleInline(viewResponse);
            }
        }
    }

    private void callLogin(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 3) {
            logger.info("Invalid number of elements in login command: " + elements.length);
            printToConsole(MessageFormat.format(messages.getString("error.validation.command.login"), elements.length));
        } else {
            boolean loginResult = httpClient.login(elements[1], elements[2]);
            if (loginResult) {
                printToConsole(messages.getString("success.command.login"));
            } else {
                printToConsole(messages.getString("failure.command.login"));
            }
        }
    }

    private void handleUnknownCommand(String cmd) {
        logger.info("Unknown command: " + cmd);
        printToConsole(MessageFormat.format(messages.getString("error.validation.command.unknown"), cmd));
    }

    private void printToConsoleInline(String message) {
        System.out.print(message);
    }

    private void printToConsole(String message) {
        System.out.println(message);
    }

    private boolean matchesCommand(String cmd, String[] commandKeywords) {
        for (String quitKeyword : commandKeywords) {
            if (quitKeyword.equalsIgnoreCase(cmd)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException, HttpClientException {
        HttpClientConsole httpClientConsole = new HttpClientConsole();
        httpClientConsole.printToConsole("HTTP Client-Console started\n");

        httpClientConsole.printToConsole("Auto-login is enabled");
        //        httpClientConsole.httpClient.login("Guest", "GuestPass");   // auto-login

        httpClientConsole.run();
    }
}
