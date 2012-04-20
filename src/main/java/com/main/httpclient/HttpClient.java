package com.main.httpclient;

import com.main.httpclient.context.Context;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Calls the XML Services REST API.
 *
 * @author Sergiu Indrie
 */
public class HttpClient {

    //    private static final String SERVER_URL = "http://localhost:8080/xml_services/";
    private static final String SERVER_URL = "http://localhost:8080/";
    private static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
    private static final String HTTP_HEADER_CACHE_CONTROL = "Cache-Control";

    private Context context;
    private Logger logger = Logger.getLogger(HttpClient.class);

    public HttpClient() {
        context = new Context();
        BasicConfigurator.configure();
    }

    public boolean login(String user, String pass) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("user=%s&pass=%s", URLEncoder.encode(user, charset), URLEncoder.encode(pass, charset));
            URL url = new URL(SERVER_URL + "login?" + query);
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
            String fullUrl = SERVER_URL + "view?" + query;

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

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                return "";
            }

            String result = getResponseContent(connection);

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
            String fullUrl = SERVER_URL + "search?" + query;

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
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(searchCriteriaInJson);
            outputStreamWriter.flush();

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                outputStreamWriter.close();
                return "";
            }

            String result = getResponseContent(connection);
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
            URL url = new URL(SERVER_URL + "update?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set cookies
            String cookies = context.getCookiesAsString();
            if (cookies != null && !(cookies.isEmpty())) {
                connection.setRequestProperty("Cookie", cookies);
                logger.debug("Adding cookies:{" + cookies + "} to request.");
            }

            String newPageContent = getContent(fileOrContent);

            // send search criteria
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(newPageContent);
            outputStreamWriter.flush();


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

    private String getContent(String fileOrContent) throws IOException {
        File file = new File(fileOrContent);
        if (file.exists() && file.isFile()) {
            return FileUtils.readFileToString(file, ClientConstants.DEFAULT_ENCODING);
        } else {
            return fileOrContent;
        }
    }


    private String getResponseContent(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

    public static void main(String[] args) throws HttpClientException {
        HttpClient httpClient = new HttpClient();
        httpClient.login("Guest", "GuestPass");
        httpClient.view("1");
    }
}
