@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.android.list

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.algolia.instantsearch.helper.android.list.internal.SearcherPaginator
import com.algolia.instantsearch.helper.android.list.internal.SearcherPagingSource
import com.algolia.instantsearch.helper.searcher.util.SearcherForHits
import com.algolia.search.model.params.SearchParameters
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.flow.Flow

/**
 * Component handling [PagingData].
 */
public interface Paginator<T : Any> {

    /**
     * Primary entry point into Paging.
     */
    public val pager: Pager<Int, T>

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

/**
 * A cold Flow of [PagingData], emits new instances of [PagingData] once they become invalidated.
 */
public val <T : Any> Paginator<T>.flow: Flow<PagingData<T>> get() = pager.flow

/**
 * A [LiveData] of [PagingData], which mirrors the stream provided by [Paginator.flow], but exposes it as a [LiveData].
 */
public val <T : Any> Paginator<T>.liveData: LiveData<PagingData<T>> get() = pager.liveData
