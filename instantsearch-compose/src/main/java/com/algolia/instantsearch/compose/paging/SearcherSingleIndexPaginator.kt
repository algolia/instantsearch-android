package com.algolia.instantsearch.compose.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * [Paginator] implementation for [SearcherSingleIndex].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
public class SearcherSingleIndexPaginator<T : Any>(
    searcher: SearcherSingleIndex,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch) -> List<T>
) : Paginator<T> {

    override val flow: Flow<PagingData<T>> = Pager(pagingConfig) {
        SearcherSingleIndexPagingSource(
            searcher = searcher,
            transformer = transformer
        )
    }.flow

    private var searcherChangeCallback: (() -> Unit)? = null

    override fun invalidate() {
        searcherChangeCallback?.invoke()
    }

    internal fun onSearcherChange(callback: () -> Unit) {
        this.searcherChangeCallback = callback
    }
}

/**
 * Collects values values from [SearcherSingleIndexPaginator].
 *
 * @param state controls and observes list scrolling
 * @param scope coroutine scope to run async operations
 */
@Composable
public fun <T : Any> SearcherSingleIndexPaginator<T>.collectAsSearcherLazyPaging(
    state: LazyListState = rememberLazyListState(),
    scope: CoroutineScope = rememberCoroutineScope()
): SearcherLazyPaging<T> {
    val pagingItems = flow.collectAsLazyPagingItems()
    return SearcherLazyPaging(pagingItems, state, scope).apply {
        onSearcherChange { resetAsync() }
    }
}
