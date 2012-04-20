package com.main.httpclient;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Command line for running the {@link HttpClient} commands.
 *
 * @author Sergiu Indrie
 */
public class HttpClientConsole {

    private Logger logger = Logger.getLogger(HttpClientConsole.class);

    private HttpClient httpClient = new HttpClient();

    private String prompt = "> ";
    // commands
    public static final String QUIT[] = new String[]{"quit", "q"};
    public static final String LOGIN[] = new String[]{"login", "l"};
    public static final String VIEW[] = new String[]{"view", "v"};
    public static final String SEARCH[] = new String[]{"search", "s"};
    public static final String UPDATE[] = new String[]{"update", "u"};

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
                printToConsole("An error has occurred during the call to HttpClient.");
            }
        } while (!quit);
    }

    private void callUpdate(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 3) {
            logger.info("Invalid number of elements in update command: " + elements.length);
            printToConsole("Invalid number of elements in update command" + elements.length);
        } else {
            boolean searchResponse = httpClient.update(elements[1], elements[2]);
            if (searchResponse) {
                printToConsole("Update successful");
            } else {
                printToConsole("Update failed");
            }
        }
    }

    private void callSearch(String command) throws HttpClientException {
        String[] elements = command.split(" ");
        if (elements.length != 3) {
            logger.info("Invalid number of elements in search command: " + elements.length);
            printToConsole("Invalid number of elements in search command" + elements.length);
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
            printToConsole("Invalid number of elements in view command" + elements.length);
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
            printToConsole("Invalid number of elements in login command" + elements.length);
        } else {
            boolean loginResult = httpClient.login(elements[1], elements[2]);
            if (loginResult) {
                printToConsole("Login successful");
            } else {
                printToConsole("Login failed");
            }
        }
    }

    private void handleUnknownCommand(String cmd) {
        logger.info("Unknown command: " + cmd);
        printToConsole(cmd + " is an unknown command");
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
        httpClientConsole.httpClient.login("Guest", "GuestPass");   // auto-login

        httpClientConsole.run();
    }
}
