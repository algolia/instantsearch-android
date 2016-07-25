package com.algolia.instantsearch.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.SearchHelper;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONObject;

/**
 * A view that can hold several hits.
 */
public interface AlgoliaResultsListener {
    /**
     * Called at initialisation to give this AlgoliaResultsListener a reference to its SearchHelper.
     *
     * @param helper an {@link SearchHelper} instance.
     */
    void onInit(@NonNull SearchHelper helper);

    /**
     * Event listener to react to new hits.
     *
     * @param hits          a {@link JSONObject} containing hits.
     * @param isLoadingMore true if these hits come from the same query than the previous ones.
     */
    void onUpdateView(@Nullable JSONObject hits, boolean isLoadingMore);

    /**
     * Event listener to react to potential search errors.
     *
     * @param query the {@link Query} that was used.
     * @param error the resulting {@link AlgoliaException}.
     */
    void onError(Query query, AlgoliaException error);
}
