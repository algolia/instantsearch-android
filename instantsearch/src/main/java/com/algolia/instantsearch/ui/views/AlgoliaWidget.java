package com.algolia.instantsearch.ui.views;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaResultsListener;
import com.algolia.instantsearch.ui.InstantSearch;

/**
 * The {@code AlgoliaWidget} interface lets you define a Widget than can be linked to an {@link InstantSearch}.
 */
public interface AlgoliaWidget extends AlgoliaResultsListener {
    /**
     * Called at initialization to give this AlgoliaResultsListener a reference to its Searcher.
     * If this is an {@link AlgoliaResultsListener}, you should setup here any listener for user interaction.
     *
     * @param searcher a {@link Searcher} instance.
     */
    void initWithSearcher(@NonNull final Searcher searcher);

    /**
     * Event listener to react to reinitialization of search interface.
     */
    void onReset();
}