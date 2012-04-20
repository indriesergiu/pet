package com.main.htmlclient.beans;

import com.main.httpclient.HttpClientException;

import javax.servlet.http.Cookie;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class UpdateBean extends AbstractBean {

    private static final String DEFAULT_START_PAGE = "0";
    private String page;
    private String pageContent;

    public void update() throws HttpClientException {
        validate();
        responseData = client.update(page, pageContent, cookieMap.values().toArray(new Cookie[0]));
    }

    private void validate() {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        } else {
            Integer.parseInt(page);
        }
    }

    public boolean getSuccess() {
        return responseData.getCode() == 200;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UpdateBean");
        sb.append("{page='").append(page).append('\'');
        sb.append(", pageContent='").append(pageContent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}