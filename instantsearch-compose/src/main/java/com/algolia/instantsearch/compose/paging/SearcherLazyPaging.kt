package com.algolia.instantsearch.compose.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Class wrapping Searcher's Lazy Paging items and state.
 */
public data class SearcherLazyPaging<T : Any>(
    public val pagingItems: LazyPagingItems<T>,
    public val listState: LazyListState,
    public val scope: CoroutineScope
) {

    /**
     * Resets the lazy paging component content and state.
     * This is done typically by refreshing the content and scrolling to the top.
     */
    public suspend fun reset() {
        pagingItems.refresh()
        listState.scrollToItem(0)
    }

    /**
     * Resets the lazy paging component content and state.
     * This is done typically by refreshing the content and scrolling to the top.
     */
    public fun resetAsync() {
        scope.launch { reset() }
    }
}
