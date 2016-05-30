package com.algolia.instantsearch.views;

import com.algolia.instantsearch.AlgoliaHelper;
import org.json.JSONObject;

/**
 * A view that can hold several hits.
 */
public interface AlgoliaResultsView {
    /**
     * Event listener to react to new hits.
     *
     * @param hits     a {@link JSONObject} containing hits
     * @param isReplacing if true, the view should dispose previous hits
     */
    void onUpdateView(JSONObject hits, boolean isReplacing);

    void onInit(AlgoliaHelper helper);
}
