package com.algolia.instantsearch.views;

import com.algolia.instantsearch.model.Result;

import java.util.Collection;

/**
 * A view that can hold several Result objects.
 */
public interface AlgoliaResultsView {
    /**
     * Event listener to react to new results. This
     * @param results a collection of Result objects
     * @param isReplacing if true, the view should dispose previous results
     */
    void onUpdateView(Collection<Result> results, boolean isReplacing);
}
