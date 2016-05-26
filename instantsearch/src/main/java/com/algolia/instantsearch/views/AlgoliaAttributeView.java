package com.algolia.instantsearch.views;

import com.algolia.instantsearch.model.Result;

/**
 * A view that can hold a hit's attribute.
 */
public interface AlgoliaAttributeView {
    /**
     * Event listener to handle a result and update your view accordingly.
     *
     * @param result the new or updated Result
     */
    void onUpdateView(Result result);
}
