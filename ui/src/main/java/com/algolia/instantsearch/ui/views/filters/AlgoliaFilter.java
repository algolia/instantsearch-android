package com.algolia.instantsearch.ui.views.filters;

import androidx.annotation.NonNull;

/**
 * Lets you define a component that will be used for displaying a facet filter.
 * <b>Note that every AlgoliaFilter must be a subtype of View.</b>
 */
public interface AlgoliaFilter {
    /**
     * Returns the faceted attribute of this facet filter.
     * <p>
     * This method is called when initializing your AlgoliaFilter.
     *
     * @return The attribute to use for faceting.
     */
    @NonNull String getAttribute();
    //DISCUSS: Would you rather have only AlgoliaWidget and document that a filter should call Searcher#set?
}
