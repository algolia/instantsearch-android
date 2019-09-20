package com.algolia.instantsearch.helper.stats

/**
 * A View that can present statistics on the current search response.
 */
public interface StatsView<T> {

    /**
     * Updates the text to display.
     */
    fun setText(text: T)
}