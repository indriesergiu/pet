package com.main.htmlclient;

import com.main.httpclient.ClientConstants;
import com.main.httpclient.HttpClientException;
import com.main.httpclient.context.Context;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

/**
 * Used by the JSP to query the XML Services server.
 *
 * @author Sergiu Indrie
 */
public class XmlServicesClient {

    private static final String SERVER_URL = "http://localhost:8080/xml_services/";
    //    private static final String SERVER_URL = "http://localhost:8080/";
    private static final String GZIP_ENCODING = "gzip";

    //    HTTP headers
    private static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
    private static final String HTTP_HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    private Context context;
    private Logger logger = Logger.getLogger(XmlServicesClient.class);

    private static XmlServicesClient singleton = new XmlServicesClient();

    private XmlServicesClient() {
        // TODO sergiu.indrie - add log conf file
        BasicConfigurator.configure();
    }

    public static XmlServicesClient getClient() {
        return singleton;
    }

    public ResponseData login(String user, String pass) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("user=%s&pass=%s", URLEncoder.encode(user, charset), URLEncoder.encode(pass, charset));
            URL url = new URL(SERVER_URL + "login?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            ResponseData responseData = new ResponseData(connection.getResponseMessage(), connection.getResponseCode());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
                Cookie[] cookies = getCookies(connection.getHeaderField(HTTP_HEADER_SET_COOKIE));
                responseData.setCookies(cookies);
            }

            return responseData;
        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    public ResponseData view(String page, Cookie[] cookies) throws HttpClientException {
        try {
            String charset = ClientConstants.DEFAULT_ENCODING;
            String query = String.format("page=%s", URLEncoder.encode(page, charset));
            String fullUrl = SERVER_URL + "view?" + query;

            // resource could not be obtained from cache
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set cookies
            String cookiesAsString = getCookiesAsString(cookies);
            if (cookies != null && !(cookies.length < 0)) {
                connection.setRequestProperty("Cookie", cookiesAsString);
                logger.debug("Adding cookies:{" + cookiesAsString + "} to request.");
            }

            connection.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, GZIP_ENCODING);

            // check request status
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info("Request was not successfully answered. Status code " + responseCode + " and message: " + connection.getResponseMessage());
                return new ResponseData(connection.getResponseMessage(), responseCode);
            }

            String result = getCompressedResponseContent(connection);

            ResponseData responseData = new ResponseData(result, responseCode, connection.getResponseMessage());

            // if cache header is present store the resource
            String cacheControlHeader = connection.getHeaderField(HTTP_HEADER_CACHE_CONTROL);
            if (cacheControlHeader != null) {
                responseData.getHeader().put(HTTP_HEADER_CACHE_CONTROL, cacheControlHeader);
            }

            return responseData;

        } catch (Exception e) {
            throw new HttpClientException("An error has occurred during authentication.", e);
        }
    }

    private String getCookiesAsString(Cookie[] cookies) {
        StringBuilder result = new StringBuilder();
        for (Cookie cookie : cookies) {
            result.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }

        if (result.length() > 0) {
            return result.substring(0, result.length() - 1).toString();
        } else {
            return "";
        }
    }

    private Cookie[] getCookies(String cookiesAsString) {
        Collection<Cookie> result = new HashSet<Cookie>();
        for (String cookie : cookiesAsString.split(";")) {
            String[] split = cookie.split("=");
            if (split.length == 2) {
                result.add(new Cookie(split[0], split[1]));
            } else {
                logger.warn("Could not store invalid cookie: " + cookie);
            }
        }
        return result.toArray(new Cookie[0]);
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
}
