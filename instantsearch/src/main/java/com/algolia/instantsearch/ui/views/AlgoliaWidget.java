package com.algolia.instantsearch.ui.views;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaResultsListener;
import com.algolia.instantsearch.ui.InstantSearch;

/**
 * Lets you define a Widget than can be linked to an {@link InstantSearch}.
 */
public interface AlgoliaWidget extends AlgoliaResultsListener {
    /**
     * Gives this AlgoliaResultsListener a reference to its Searcher when it is initialized.
     * <p>
     * You should setup here any listener for user interaction.
     *
     * @param searcher a {@link Searcher} instance.
     */
    void initWithSearcher(@NonNull final Searcher searcher);
}