package com.algolia.instantsearch.core.hits

/**
 * A View that can display a list of Hits.
 */
public interface HitsView<T> {

    /**
     * Updates the Hits to display.
     */
    public fun setHits(hits: List<T>)
}