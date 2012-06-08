package com.xmlservices.jspclient.htmlclient.beans;

import com.xmlservices.jspclient.htmlclient.beans.restclient.XmlServicesClient;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;

/**
 * Provides basic behavior needed by server beans such as whether the response object (status, cookies etc.).
 *
 * @author Sergiu Indrie
 */
public class AbstractBean {

    protected Logger logger = Logger.getLogger(AbstractBean.class);
    protected ResponseData responseData;
    protected XmlServicesClient client = XmlServicesClient.getClient();
    protected Map<String, Cookie> cookieMap;

    public boolean getSuccess() {
        return responseData.getCode() == HttpURLConnection.HTTP_OK;
    }

    public boolean isUnauthenticated() {
        return responseData.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    public boolean isNotFound() {
        return responseData.getCode() == HttpURLConnection.HTTP_NOT_FOUND;
    }

    public int getResponseStatusCode() {
        return responseData.getCode();
    }

    public String getResponseMessage() {
        return responseData.getMessage();
    }

    public Cookie[] getCookies() {
        return responseData.getCookies();
    }

    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, Cookie> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String> getHeaderMap() {
        if (responseData == null) {
            return Collections.emptyMap();
        }
        return responseData.getHeader();
    }
}
