@file:Suppress("FunctionName")

package com.algolia.instantsearch.android.paging3

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.algolia.instantsearch.android.paging3.internal.SearcherPaginator
import com.algolia.instantsearch.android.paging3.internal.SearcherPagingSource
import com.algolia.instantsearch.helper.searcher.util.SearcherForHits
import com.algolia.search.model.params.SearchParameters
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
 * Paginator for [SearcherForHits].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
public fun <T : Any> Paginator(
    searcher: SearcherForHits<out SearchParameters>,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch.Hit) -> T
): Paginator<T> = SearcherPaginator(
    pagingConfig = pagingConfig,
    pagingSourceFactory = { SearcherPagingSource(searcher, transformer) }
)
