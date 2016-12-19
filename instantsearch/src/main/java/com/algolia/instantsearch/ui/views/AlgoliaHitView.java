package com.algolia.instantsearch.ui.views;

import org.json.JSONObject;


/**
 * The {@code AlgoliaHitView} interface lets you define a component that will be used for displaying a hit.
 * <p>
 * Intended to be implemented by a {@link android.view.View}, it lets you process the hit JSONObject and decide how your view should display it.
 */
public interface AlgoliaHitView {
    /**
     * Event listener to handle a result and update your view accordingly.
     *
     * @param result a {@link JSONObject} describing a hit
     */
    void onUpdateView(JSONObject result);
}
