package com.main.htmlclient.beans;

/**
 * Used by the {@link SearchBean}.
 *
 * @author Sergiu Indrie
 */
public class SearchParameter {
    public String name;
    public String value;

    SearchParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
