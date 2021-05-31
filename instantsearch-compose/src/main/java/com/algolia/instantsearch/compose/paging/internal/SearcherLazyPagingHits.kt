package com.algolia.instantsearch.compose.paging.internal

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.compose.LazyPagingItems
import com.algolia.instantsearch.compose.paging.LazyListPagingHits
import com.algolia.instantsearch.compose.paging.Paginator

/**
 * Implementation of [LazyListPagingHits].
 *
 * @param pagingItems responsible for accessing the data from PagingData
 * @param listState controls and observes list scrolling
 * @param paginator component handling Paged data
 */
internal class SearcherLazyPagingHits<T : Any>(
    override val pagingItems: LazyPagingItems<T>,
    override val listState: LazyListState,
    private val paginator: Paginator<T>
) : LazyListPagingHits<T> {

    override suspend fun reset() {
        paginator.invalidate()
        listState.scrollToItem(0)
    }
}
