package com.algolia.instantsearch.core.model;

import androidx.annotation.NonNull;

/**
 * Lets you define a component that will react to search results.
 */
public interface AlgoliaResultsListener {
    /**
     * Reacts to new hits.
     *
     * @param results       a {@link SearchResults} object containing hits.
     * @param isLoadingMore {@code true} if these hits come from the same query than the previous ones.
     */
    void onResults(@NonNull final SearchResults results, boolean isLoadingMore);
}