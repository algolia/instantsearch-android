package com.algolia.instantsearch.model;

import com.algolia.instantsearch.ui.HighlightRenderer;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<String, String> attributes;

    public Result() {
        attributes = new HashMap<>();
    }

    /**
     * Set or replace an attribute
     *
     * @param attributeName the attribute's name
     * @param value         its value
     */
    public void set(String attributeName, String value) {
        attributes.put(attributeName, value);
    }

    /**
     * Get an attribute without its highlight(s)
     *
     * @param attributeName the attribute's name
     * @return the raw value of this attribute
     */
    public String get(String attributeName) {
        return get(attributeName, false);
    }

    /**
     * Get an attribute, potentially highlighted
     *
     * @param attributeName the attribute's name
     * @param withHighlight if true, the value will be returned with its highlight(s)
     * @return the (potentially highlighted) value of this attribute
     */
    public String get(String attributeName, boolean withHighlight) {
        final String attribute = attributes.get(attributeName);
        if (attribute != null && !withHighlight) {
            return HighlightRenderer.removeHighlight(attribute);
        }
        return attribute;
    }
}
