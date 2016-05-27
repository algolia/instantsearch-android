package com.algolia.instantsearch.views;

import org.json.JSONObject;

/**
 * A view that can hold a hit's attribute.
 */
public interface AlgoliaAttributeView {
    /**
     * Event listener to handle a result and update your view accordingly.
     *
     * @param result a {@link JSONObject} describing a hit
     */
    void onUpdateView(JSONObject result);
}
