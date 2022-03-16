@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.list

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.compose.list.internal.SearcherPaginator
import com.algolia.instantsearch.compose.list.internal.SearcherPagingSource
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import com.algolia.search.model.params.SearchParameters
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.flow.Flow

/**
 * Component handling [PagingData].
 */
@ExperimentalInstantSearch
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
 * Paginator for [SearcherForHits].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
@ExperimentalInstantSearch
public fun <T : Any> Paginator(
    searcher: SearcherForHits<out SearchParameters>,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch.Hit) -> T
): Paginator<T> = SearcherPaginator(
    pagingConfig = pagingConfig,
    pagingSourceFactory = { SearcherPagingSource(searcher, transformer) }
)
