@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.compose.paging.internal.SearcherPaginator
import com.algolia.instantsearch.compose.paging.internal.SearcherSingleIndexPaginator
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Component handling Paged data.
 */
public interface Paginator<T : Any> {

    /**
     * A cold Flow of [PagingData].
     * Emits new instances of [PagingData] once they become invalidated.
     */
    public val flow: Flow<PagingData<T>>

    /**
     * Signal the [Paginator] to stop loading.
     */
    public fun invalidate()
}

/**
 * Paginator for [SearcherSingleIndex].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
public fun <T : Any> Paginator(
    searcher: SearcherSingleIndex,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch) -> List<T>
): Paginator<T> {
    return SearcherSingleIndexPaginator(searcher, pagingConfig, transformer)
}

/**
 * Collects values values from [SearcherSingleIndexPaginator].
 *
 * @param state controls and observes list scrolling
 * @param scope coroutine scope to run async operations
 */
@Composable
public fun <T : Any> Paginator<T>.collectAsSearcherLazyPaging(
    state: LazyListState = rememberLazyListState(),
    scope: CoroutineScope = rememberCoroutineScope()
): SearcherLazyPaging<T> {
    val pagingItems = flow.collectAsLazyPagingItems()
    return SearcherLazyPaging(pagingItems, state, scope).apply {
        if (this is SearcherPaginator<*>) onSearcherChange { resetAsync() }
    }
}
