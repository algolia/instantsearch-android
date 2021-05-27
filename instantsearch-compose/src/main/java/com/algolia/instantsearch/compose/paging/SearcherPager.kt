package com.algolia.instantsearch.compose.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * Pager for Search. Handles data paging logic and reset on searcher changes.
 */
public interface SearcherPager<T : Any> {

    /**
     * A cold Flow of PagingData, which emits new instances of PagingData once they become invalidated.
     */
    public val flow: Flow<PagingData<T>>

    /**
     * Notify searcher's configuration has changed.
     */
    public fun notifySearcherChanged()
}
