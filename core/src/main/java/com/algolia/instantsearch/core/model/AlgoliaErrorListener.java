package com.algolia.instantsearch.core.model;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import androidx.annotation.NonNull;

/**
 * Lets you define a component that will react to search errors.
 */
public interface AlgoliaErrorListener {
    /**
     * Reacts to potential search errors.
     *
     * @param query the {@link Query} that was used.
     * @param error the resulting {@link AlgoliaException}.
     */
    void onError(@NonNull final Query query, @NonNull final AlgoliaException error);
}