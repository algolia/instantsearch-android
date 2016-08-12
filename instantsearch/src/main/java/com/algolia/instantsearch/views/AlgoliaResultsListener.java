package com.algolia.instantsearch.views;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

/**
 * A view that can hold several hits.
 */
public interface AlgoliaResultsListener {
    /**
     * Event listener to react to new hits.
     *
     * @param results       a {@link SearchResults} object containing hits.
     * @param isLoadingMore true if these hits come from the same query than the previous ones.
     */
    void onResults(final SearchResults results, boolean isLoadingMore);

    /**
     * Event listener to react to potential search errors.
     *
     * @param query the {@link Query} that was used.
     * @param error the resulting {@link AlgoliaException}.
     */
    void onError(final Query query, final AlgoliaException error);
}