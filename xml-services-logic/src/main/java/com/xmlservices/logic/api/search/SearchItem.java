package com.xmlservices.logic.api.search;

/**
 * A single search item containing type and value.
 *
 * @author sergiu.indrie
 */
public class SearchItem {

    private SearchItemType searchItemType;
    private String value;

    public SearchItem() {
    }

    public SearchItem(SearchItemType searchItemType, String value) {
        this.searchItemType = searchItemType;
        this.value = value;
    }

    public SearchItemType getSearchItemType() {
        return searchItemType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SearchItem{" + "searchItemType=" + searchItemType + ", value='" + value + '\'' + '}';
    }
}
