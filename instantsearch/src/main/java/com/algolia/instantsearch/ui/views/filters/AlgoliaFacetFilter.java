package com.algolia.instantsearch.ui.views.filters;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.AlgoliaWidget;

public interface AlgoliaFacetFilter extends AlgoliaWidget{
    /**
     * This method is called when initialising your AlgoliaFacetFilter to register its facet.
     * @return The attribute to use for faceting.
     */
    @NonNull String getAttributeName();
    //DISCUSS: Would you rather have only AlgoliaWidget
    // and document that a filter should call Searcher#set

    /**
     * This method is called when registering your filters. You should define interaction listeners
     * here to act on user input and update Searcher's facets,
     * for example with {@link Searcher#updateFacetRefinement(String, String, boolean) updateFacetRefinement}.
     *
     * @param searcher the Searcher used in your search interface.
     */
}
