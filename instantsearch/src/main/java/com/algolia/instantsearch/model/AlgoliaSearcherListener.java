package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.helpers.Searcher;

public interface AlgoliaSearcherListener {
    /**
     * Reacts to the initialization of a Searcher.
     * <p>
     * You should setup here any listener for user interaction.
     *
     * @param searcher a {@link Searcher} instance.
     */
    void initWithSearcher(@NonNull final Searcher searcher);
}
