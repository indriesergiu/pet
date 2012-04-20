package com.main.htmlclient.beans;

import com.main.htmlclient.ResponseData;
import com.main.htmlclient.XmlServicesClient;

import javax.servlet.http.Cookie;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class AbstractBean {
    protected ResponseData responseData;
    protected XmlServicesClient client = XmlServicesClient.getClient();

    public boolean getSuccess() {
        return responseData.getCode() == 202;
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
}
