@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.list

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.algolia.instantsearch.compose.list.internal.SearcherSingleIndexPaginator
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.flow.Flow

/**
 * Component handling [PagingData].
 */
public interface Paginator<T : Any> {

    /**
     * A cold Flow of [PagingData], emits new instances of [PagingData] once they become invalidated.
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
