package com.main.htmlclient.beans;

import com.main.httpclient.HttpClientException;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Holds view fields from UI, calls the view service call and presents the call results(the page content) to the UI.
 * <p/>
 * See page 'view.jsp'
 *
 * @author Sergiu Indrie
 */
public class ViewBean extends AbstractBean {

    private static final String DEFAULT_START_PAGE = "0";
    private String page;

    public void view() throws HttpClientException {
        validate();
        responseData = client.view(page, cookieMap.values().toArray(new Cookie[0]));
    }

    private void validate() {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        } else {
            Integer.parseInt(page);
        }
    }

    public String getPageContent() {
        return StringEscapeUtils.escapeHtml4(responseData.getBody());
    }

    public String getUrlEncodedPageContent() throws UnsupportedEncodingException {
        return URLEncoder.encode(StringEscapeUtils.escapeHtml4(responseData.getBody()), "UTF-8");
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNextPage() {
        return String.valueOf(Integer.valueOf(page) + 1);
    }

    public String getPreviousPage() {
        Integer pageAsInteger = Integer.valueOf(page);
        return pageAsInteger > 0 ? String.valueOf(pageAsInteger - 1) : "";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ViewBean");
        sb.append("{page='").append(page).append('\'');
        sb.append(", cookieMap=").append(cookieMap);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeHtml4("<option value=\"Attribute\">Attribute</option>\n" + "<option value=\"Data\">Data</option>"));
    }
}
