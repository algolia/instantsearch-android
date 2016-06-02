package com.algolia.instantsearch.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.AlgoliaHelper;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONObject;

/**
 * A view that can hold several hits.
 */
public interface AlgoliaResultsView {
    /**
     * Called at initialisation to give this AlgoliaResultsView a reference to its AlgoliaHelper.
     *
     * @param helper an {@link AlgoliaHelper} instance.
     */
    void onInit(@NonNull AlgoliaHelper helper);

    /**
     * Event listener to react to new hits
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
