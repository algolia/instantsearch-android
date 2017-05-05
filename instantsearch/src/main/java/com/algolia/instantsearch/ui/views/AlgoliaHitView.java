package com.algolia.instantsearch.ui.views;

import org.json.JSONObject;


/**
 * Lets you define a component that will be used for displaying a hit.
 * <p>
 * Intended to be implemented by a {@link android.view.View}, it lets you process the hit JSONObject and decide how your view should display it.
 */
public interface AlgoliaHitView {
    /**
     * Handles a result and updates your view to display the result.
     *
     * @param result a {@link JSONObject} describing a hit.
     */
    void onUpdateView(final JSONObject result);
}
