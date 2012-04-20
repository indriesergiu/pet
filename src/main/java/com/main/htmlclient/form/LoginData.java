package com.main.htmlclient.form;

import com.main.htmlclient.ResponseData;
import com.main.htmlclient.XmlServicesClient;
import com.main.httpclient.HttpClientException;

import javax.servlet.http.Cookie;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class LoginData {

    private String username;
    private String password;

    private ResponseData responseData;

    private XmlServicesClient client = XmlServicesClient.getClient();

    public void login() throws HttpClientException {
        responseData = client.login(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LoginData");
        sb.append("{username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
