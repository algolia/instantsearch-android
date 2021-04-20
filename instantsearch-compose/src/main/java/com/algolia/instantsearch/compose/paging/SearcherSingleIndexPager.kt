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
 * [SearcherPager] implementation for [SearcherSingleIndex].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
public class SearcherSingleIndexPager<T : Any>(
    searcher: SearcherSingleIndex,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch) -> List<T>
) : SearcherPager<T> {

    override val flow: Flow<PagingData<T>> = Pager(pagingConfig) {
        SearcherSingleIndexPagingSource(
            searcher = searcher,
            transformer = transformer
        )
    }.flow

    private var callback: (() -> Unit)? = null

    override fun notifySearcherChanged() {
        callback?.invoke()
    }

    internal fun onSearcherChange(callback: () -> Unit) {
        this.callback = callback
    }
}

/**
 * Collects values values from [SearcherSingleIndexPager].
 *
 * @param state controls and observes list scrolling
 * @param scope coroutine scope to run async operations
 */
@Composable
public fun <T : Any> SearcherSingleIndexPager<T>.collectAsSearcherLazyPaging(
    state: LazyListState = rememberLazyListState(),
    scope: CoroutineScope = rememberCoroutineScope()
): SearcherLazyPaging<T> {
    val pagingItems = flow.collectAsLazyPagingItems()
    val searcherLazyPaging = SearcherLazyPaging(pagingItems, state, scope)
    onSearcherChange { searcherLazyPaging.resetAsync() }
    return searcherLazyPaging
}
