package com.algolia.instantsearch.compose.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.compose.LazyPagingItems

/**
 * Class wrapping lazy hits paging and list state.
 */
public interface LazyListPagingHits<T : Any> {

    /**
     * Data to display, received from the Flow of PagingData.
     */
    public val pagingItems: LazyPagingItems<T>

    /**
     * A state object to control and observe list scrolling.
     */
    public val listState: LazyListState

    /**
     * Resets the lazy paging component content and state.
     * This is done typically by refreshing the content and scrolling to the top.
     */
    public suspend fun reset()
}
