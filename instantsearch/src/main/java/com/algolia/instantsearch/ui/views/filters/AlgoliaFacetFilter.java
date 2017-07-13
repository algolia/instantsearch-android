package com.algolia.instantsearch.ui.views.filters;

import android.support.annotation.NonNull;

/**
 * Lets you define a component that will be used for displaying a facet filter.
 */
public interface AlgoliaFacetFilter {
    /**
     * Returns the faceted attribute of this facet filter.
     * <p>
     * This method is called when initializing your AlgoliaFacetFilter.
     *
     * @return The attribute to use for faceting.
     */
    @NonNull String getAttributeName();
    //DISCUSS: Would you rather have only AlgoliaWidget and document that a filter should call Searcher#set?
}
