package com.algolia.instantsearch.views;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.Searcher;

public interface AlgoliaWidget extends AlgoliaResultsListener {
    /**
     * Called at initialisation to give this AlgoliaResultsListener a reference to its Searcher.
     *
     * @param searcher an {@link Searcher} instance.
     */
    void setSearcher(@NonNull final Searcher searcher);

    /**
     * Event listener to react to reinitialization of search interface.
     */
    void onReset();
}