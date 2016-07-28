package com.algolia.instantsearch.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.Searcher;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONObject;

/**
 * A view that can hold several hits.
 */
public interface AlgoliaResultsListener {
    /**
     * Called at initialisation to give this AlgoliaResultsListener a reference to its Searcher.
     *
     * @param searcher an {@link Searcher} instance.
     */
    void onInit(@NonNull final Searcher searcher);

    /**
     * Event listener to react to new hits.
     *
     * @param hits          a {@link JSONObject} containing hits.
     * @param isLoadingMore true if these hits come from the same query than the previous ones.
     */
    void onUpdateView(@Nullable final JSONObject hits, boolean isLoadingMore);

    /**
     * Event listener to react to potential search errors.
     *
     * @param query the {@link Query} that was used.
     * @param error the resulting {@link AlgoliaException}.
     */
    void onError(final Query query, final AlgoliaException error);

    /**
     * Event listener to react to reinitialization of search interface.
     */
    void onReset();
}
