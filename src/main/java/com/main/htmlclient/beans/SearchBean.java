package com.main.htmlclient.beans;

import com.main.httpclient.HttpClientException;
import com.main.xmlfilter.search.SearchCriteria;
import com.main.xmlfilter.search.SearchItem;
import com.main.xmlfilter.search.SearchItemType;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used by search_results.jsp.
 *
 * @author Sergiu Indrie
 */
public class SearchBean extends AbstractBean {

    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private String page;
    private Map<String, String> parameters;
    private static final String DEFAULT_START_PAGE = "0";
    private List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();

    public void search() throws HttpClientException {
        SearchCriteria searchCriteria = buildSearchCriteria(parameters);
        validate(searchCriteria);
        responseData = client.search(page, searchCriteria, cookieMap.values().toArray(new Cookie[0]));
    }

    private void validate(SearchCriteria searchCriteria) {
        if (page == null) {
            logger.info("No page value sent.");
            page = DEFAULT_START_PAGE;
        } else {
            Integer.parseInt(page);
        }

        if (searchCriteria.getSearchItems().isEmpty()) {
            logger.error("No search criteria has been sent.");
            throw new IllegalArgumentException("No search criteria has been sent. At least one search rule must be entered.");
        }
    }

    private SearchCriteria buildSearchCriteria(Map<String, String> parameters) {
        SearchCriteria searchCriteria = new SearchCriteria();

        for (int i = 0; ; i++) {
            String typeKey = TYPE + i;
            String valueKey = VALUE + i;
            if (parameters.containsKey(typeKey) && parameters.containsKey(valueKey)) {
                String type = parameters.get(typeKey);
                String value = parameters.get(valueKey);
                searchCriteria.getSearchItems().add(new SearchItem(SearchItemType.valueOf(type.toUpperCase()), value));

                // store the search parameters for next/previous links
                searchParameters.add(new SearchParameter(typeKey, type));
                searchParameters.add(new SearchParameter(valueKey, value));
            } else {
                break;
            }
        }

        logger.info("Created search criteria: " + searchCriteria);

        return searchCriteria;
    }

    public String getPageContent() {
        return StringEscapeUtils.escapeHtml4(responseData.getBody());
    }

    public String getPage() {
        return page;
    }

    public String getNextPage() {
        return String.valueOf(Integer.valueOf(page) + 1);
    }

    public String getPreviousPage() {
        Integer pageAsInteger = Integer.valueOf(page);
        return pageAsInteger > 0 ? String.valueOf(pageAsInteger - 1) : "";
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public List<SearchParameter> getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(List<SearchParameter> searchParameters) {
        this.searchParameters = searchParameters;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SearchBean");
        sb.append("{page='").append(page).append('\'');
        sb.append(", parameters=").append(parameters);
        sb.append(", searchParameters=").append(searchParameters);
        sb.append('}');
        return sb.toString();
    }
}
