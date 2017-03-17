package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

/**
 * The {@code AlgoliaResultsListener} interface lets you define a component that will react to search results or errors.
 */
public interface AlgoliaResultsListener {
    /**
     * Event listener to react to new hits.
     *
     * @param results       a {@link SearchResults} object containing hits.
     * @param isLoadingMore true if these hits come from the same query than the previous ones.
     */
    void onResults(@NonNull final SearchResults results, boolean isLoadingMore);

    /**
     * Event listener to react to potential search errors.
     *
     * @param query the {@link Query} that was used.
     * @param error the resulting {@link AlgoliaException}.
     */
    void onError(final Query query, final AlgoliaException error);
}