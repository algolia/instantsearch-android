package com.algolia.instantsearch.model;

import com.algolia.instantsearch.model.Highlight;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<String, String> attributes;
    private Map<String, Highlight> highlights;

    public Result() {
        attributes = new HashMap<>();
        highlights = new HashMap<>();
    }

    public void set(String key, String value) {
        attributes.put(key, value);
    }

    public String get(String key) {
        return attributes.get(key);
    }


    public Highlight getHighlight(String attributeName) {
        return highlights.get(attributeName);
    }

    public void addHighlight(Highlight highlight) {
        highlights.put(highlight.getAttributeName(), highlight);
    }

}
