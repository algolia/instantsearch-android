package com.algolia.instantsearch.model;

import android.util.Pair;

import com.algolia.instantsearch.ui.HighlightRenderer;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<String, Pair<String, Boolean>> attributes;

    public Result() {
        attributes = new HashMap<>();
    }

    /**
     * Set or replace an attribute (without any highlight)
     *
     * @param attributeName the attribute's name
     * @param value         its value
     */
    public void set(String attributeName, String value) {
        set(attributeName, value, false);
    }

    /**
     * Set or replace an attribute
     *
     * @param attributeName the attribute's name
     * @param value         its value
     * @param highlighted   true if the value contains highlight(s)
     */
    public void set(String attributeName, String value, Boolean highlighted) {
        attributes.put(attributeName, new Pair<>(value, highlighted));
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
        final String attribute = attributes.get(attributeName).first;
        if (!withHighlight) {
            return HighlightRenderer.removeHighlight(attribute);
        }
        return attribute;
    }

    /**
     * Get the highlighted status of an attribute
     * @param attributeName the attribute's name
     * @return true if the attribute was set with highlight(s)
     */
    public boolean hasHighlighted(String attributeName) {
        return attributes.get(attributeName).second;
    }
}
