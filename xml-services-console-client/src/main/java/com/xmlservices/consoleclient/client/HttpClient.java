package com.xmlservices.consoleclient.client;

import com.xmlservices.consoleclient.client.context.Context;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Calls the XML Services REST API.
 *
 * @author Sergiu Indrie
 */
public class HttpClient {

    //    HTTP headers
    private static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
    private static final String HTTP_HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    private static final String GZIP_ENCODING = "gzip";

    //   Configuration properties
    private static final String PROPERTY_serverUrl = "server.url";

    private String serverUrl;

    private Context context;
    private Logger logger = Logger.getLogger(HttpClient.class);

    public HttpClient() throws IOException {
        context = new Context();
        BasicConfigurator.configure();
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
        serverUrl = properties.getProperty(PROPERTY_serverUrl);
        
        validateConfiguration();
    }

    private void validateConfiguration() {
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid " + PROPERTY_serverUrl + " value=" + serverUrl);
        }
        logger.debug("Configuration was successfully validated.");
    }

    public boolean login(String user, String pass) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("user=%s&pass=%s", URLEncoder.encode(user, charset), URLEncoder.encode(pass, charset));
            URL url = new URL(serverUrl + "login?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // read cookies
            String cookies = getCookies(connection);
            if (cookies != null && !(cookies.isEmpty())) {
                context.addCookiesFromString(cookies);
                logger.debug("Cookies received: " + cookies);
            }

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                logger.info("Authentication request denied. Response message=" + connection.getResponseMessage());
                return false;
            } else if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                logger.info("Authentication successful. Response message=" + connection.getResponseMessage());
                return true;
            } else {
                logger.info("Authentication request failed with status code " + responseCode + ". Response message=" + connection.getResponseMessage());
                return false;
            }

        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    public String view(String page) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("page=%s", URLEncoder.encode(page, charset));
            String fullUrl = serverUrl + "view?" + query;

            // see if request can be obtained from cache
            String content = context.getCachedResource(fullUrl);
            if (content != null) {
                return content;
            }

            // resource could not be obtained from cache
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set cookies
            String cookies = context.getCookiesAsString();
            if (cookies != null && !(cookies.isEmpty())) {
                connection.setRequestProperty("Cookie", cookies);
                logger.debug("Adding cookies:{" + cookies + "} to request.");
            }

            connection.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, GZIP_ENCODING);

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                return "";
            }

            String result = getCompressedResponseContent(connection);

            // if cache header is present store the resource
            String cacheControlHeader = connection.getHeaderField(HTTP_HEADER_CACHE_CONTROL);
            if (cacheControlHeader != null) {
                context.storeResourceInCache(fullUrl, cacheControlHeader, result);
            }

            return result;

        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    public String search(String page, String searchCriteriaInJson) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("page=%s", URLEncoder.encode(page, charset));
            String fullUrl = serverUrl + "search?" + query;

            // see if request can be obtained from cache
            String content = context.getCachedResource(fullUrl);
            if (content != null) {
                return content;
            }

            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set cookies
            String cookies = context.getCookiesAsString();
            if (cookies != null && !(cookies.isEmpty())) {
                connection.setRequestProperty("Cookie", cookies);
                logger.debug("Adding cookies:{" + cookies + "} to request.");
            }

            // send search criteria
            Closeable outputStreamWriter = sendCompressedRequestBody(connection, searchCriteriaInJson);

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                outputStreamWriter.close();
                return "";
            }

            String result = getCompressedResponseContent(connection);
            outputStreamWriter.close();

            // if cache header is present store the resource
            String cacheControlHeader = connection.getHeaderField(HTTP_HEADER_CACHE_CONTROL);
            if (cacheControlHeader != null) {
                context.storeResourceInCache(fullUrl, cacheControlHeader, result);
            }

            return result;

        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    public boolean update(String page, String fileOrContent) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("page=%s", URLEncoder.encode(page, charset));
            URL url = new URL(serverUrl + "update?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set cookies
            String cookies = context.getCookiesAsString();
            if (cookies != null && !(cookies.isEmpty())) {
                connection.setRequestProperty("Cookie", cookies);
                logger.debug("Adding cookies:{" + cookies + "} to request.");
            }

            String newPageContent = getContent(fileOrContent);

            // send search criteria
            Closeable outputStreamWriter = sendCompressedRequestBody(connection, newPageContent);

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                outputStreamWriter.close();
                return false;
            }

            outputStreamWriter.close();
            return true;

        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    private Closeable sendPlainRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStream outputStream = connection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        outputStreamWriter.write(requestBody);
        outputStreamWriter.flush();
        return outputStreamWriter;
    }

    private Closeable sendCompressedRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, "gzip");
        connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, "application/gzip");
        OutputStream outputStream = connection.getOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gzipOutputStream);
        outputStreamWriter.write(requestBody);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        return gzipOutputStream;
    }

    private String getContent(String fileOrContent) throws IOException {
        File file = new File(fileOrContent);
        if (file.exists() && file.isFile()) {
            return FileUtils.readFileToString(file, ClientConstants.DEFAULT_ENCODING);
        } else {
            return fileOrContent;
        }
    }

    private String getPlainResponseContent(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        String temp;

        while ((temp = in.readLine()) != null) {
            result.append(temp + "\n");
        }
        in.close();
        return result.toString();
    }

    private String getCompressedResponseContent(HttpURLConnection connection) throws IOException {
        // TODO sergiu.indrie - check type of compression
        BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
        StringBuilder result = new StringBuilder();
        String temp;

        while ((temp = in.readLine()) != null) {
            result.append(temp + "\n");
        }
        in.close();
        return result.toString();
    }

    private String getCookies(URLConnection urlConnection) {
        return urlConnection.getHeaderField(HTTP_HEADER_SET_COOKIE);
    }

    public static void main(String[] args) throws HttpClientException, IOException {
        HttpClient httpClient = new HttpClient();
        httpClient.login("Guest", "GuestPass");
        httpClient.view("1");
    }
}
