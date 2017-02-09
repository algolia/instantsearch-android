package com.algolia.instantsearch.ui.views.filters;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.helpers.Searcher;

public interface AlgoliaFacetFilter {
    @NonNull String getAttribute();

    /**
     * This method is called when registering your filters. You should define interaction listeners
     * here to act on user input and update Searcher's facets,
     * for example with {@link Searcher#updateFacetRefinement(String, String, boolean) updateFacetRefinement}.
     *
     * @param searcher the Searcher used in your search interface.
     */
    @NonNull void defineListeners(final Searcher searcher);
}
