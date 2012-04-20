package com.main.htmlclient.beans;

import com.main.htmlclient.ResponseData;
import com.main.htmlclient.XmlServicesClient;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class AbstractBean {

    protected Logger logger = Logger.getLogger(AbstractBean.class);
    protected ResponseData responseData;
    protected XmlServicesClient client = XmlServicesClient.getClient();
    protected Map<String, Cookie> cookieMap;

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

    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, Cookie> cookieMap) {
        this.cookieMap = cookieMap;
    }
}
