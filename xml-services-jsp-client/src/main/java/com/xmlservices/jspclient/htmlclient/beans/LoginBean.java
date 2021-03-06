package com.xmlservices.jspclient.htmlclient.beans;

import com.xmlservices.jspclient.htmlclient.beans.restclient.XmlServicesClientException;

import java.net.HttpURLConnection;

/**
 * Holds login fields from UI, calls the login service call and presents the call results to the UI.
 *
 * @author Sergiu Indrie
 */
public class LoginBean extends AbstractBean {

    private String username;
    private String password;

    public void login() throws XmlServicesClientException {
        responseData = client.login(username, password);
    }

    public boolean getSuccess() {
        return responseData.getCode() == HttpURLConnection.HTTP_ACCEPTED;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LoginBean");
        sb.append("{username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
