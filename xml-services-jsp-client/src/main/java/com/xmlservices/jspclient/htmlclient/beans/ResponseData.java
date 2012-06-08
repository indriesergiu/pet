package com.xmlservices.jspclient.htmlclient.beans;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Response object returned by the {@link XmlServicesClient} calls.
 *
 * @author Sergiu Indrie
 */
public class ResponseData {

    private String message;
    private int code;
    private Map<String, String> header;
    private String body;
    private Cookie[] cookies;

    public ResponseData(String message, int code) {
        this.message = message;
        this.code = code;
        header = new HashMap<String, String>();
    }

    public ResponseData(String body, int code, String message) {
        this.body = body;
        this.code = code;
        this.message = message;
        header = new HashMap<String, String>();
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ResponseData");
        sb.append("{message='").append(message).append('\'');
        sb.append(", code=").append(code);
        sb.append(", header=").append(header);
        sb.append(", body='").append(body).append('\'');
        sb.append(", cookies=").append(cookies == null ? "null" : Arrays.asList(cookies).toString());
        sb.append('}');
        return sb.toString();
    }
}
