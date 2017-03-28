package com.algolia.instantsearch.ui.views.filters;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.ui.views.AlgoliaWidget;

/**
 * Lets you define a component that will be used for displaying a facet filter.
 */
public interface AlgoliaFacetFilter extends AlgoliaWidget {
    /**
     * Returns the faceted attribute of this facet filter.
     * <p>
     * This method is called when initializing your AlgoliaFacetFilter.
     *
     * @return The attribute to use for faceting.
     */
    @NonNull String getAttributeName();
    //DISCUSS: Would you rather have only AlgoliaWidget and document that a filter should call Searcher#set?
    //TODO: if two widgets are the same, ensure changing one updates the other
}
