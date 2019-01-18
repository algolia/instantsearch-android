package com.algolia.instantsearch.core.model;

import com.algolia.instantsearch.core.helpers.Searcher;

import androidx.annotation.NonNull;

public interface AlgoliaSearcherListener {
    /**
     * Reacts to the initialization of the Searcher.
     * This interface allows widgets to update the state of the search interface
     * by giving them a reference to its Searcher.
     * <p>
     * You should setup here any listener for user interaction.
     *
     * @param searcher a {@link Searcher} instance.
     */
    void initWithSearcher(@NonNull final Searcher searcher);
}
