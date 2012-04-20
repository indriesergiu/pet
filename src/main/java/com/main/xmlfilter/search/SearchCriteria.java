package com.main.xmlfilter.search;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Holds all search necessary information (lists of attribute-value pairs)
 *
 * @author sergiu.indrie
 */
public class SearchCriteria {

    private Collection<SearchItem> searchItems;

    public SearchCriteria() {
        searchItems = new LinkedList<SearchItem>();
    }

    // todo postpone creation until servlet entry format is known

    public Collection<SearchItem> getSearchItems() {
        return searchItems;
    }

    public boolean matchAttribute(String attribute) {
        if (attribute == null) {
            return false;
        }
        for (SearchItem searchItem : searchItems) {
            switch (searchItem.getSearchItemType()) {
                case ATTRIBUTE:
                    if (attribute.toLowerCase().contains(searchItem.getValue().toLowerCase())) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public boolean matchData(String data) {
        if (data == null) {
            return false;
        }
        for (SearchItem searchItem : searchItems) {
            switch (searchItem.getSearchItemType()) {
                case DATA:
                    if (data.toLowerCase().contains(searchItem.getValue().toLowerCase())) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Creates a search criteria based on the given value. The criteria will search for data or attribute values that match the given value.
     *
     * @param value the value to create the search criteria from
     * @return a search criteria based on the given value
     */
    public static SearchCriteria createSearchCriteriaFromValue(String value) {
        SearchCriteria result = new SearchCriteria();
        result.getSearchItems().add(new SearchItem(SearchItemType.ATTRIBUTE, value));
        result.getSearchItems().add(new SearchItem(SearchItemType.DATA, value));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SearchCriteria");
        sb.append("{searchItems=").append(searchItems);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String searchCriteriaInJson = mapper.writeValueAsString(createSearchCriteriaFromValue("apple"));
        System.out.println(searchCriteriaInJson);
    }
}
