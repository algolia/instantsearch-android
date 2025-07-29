@file:Suppress("FunctionName")

package com.algolia.instantsearch.android.paging3

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.algolia.instantsearch.android.paging3.internal.SearcherPaginator
import com.algolia.instantsearch.android.paging3.internal.SearcherPagingSource
import com.algolia.instantsearch.extension.Console
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.params.SearchParameters
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.flow.Flow

/**
 * Component handling [PagingData].
 */
public interface Paginator<T : Any> {

    /**
     * Primary entry point into Paging; constructor for a reactive stream of [PagingData].
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
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 10),
    transformer: (ResponseSearch.Hit) -> T
): Paginator<T> {
    if (pagingConfig.initialLoadSize != pagingConfig.pageSize) {
        Console.warn("Overriding initialLoadSize (${pagingConfig.initialLoadSize}) to match pageSize (${pagingConfig.pageSize})")
    }

    val validPagingConfig = PagingConfig(
        pageSize = pagingConfig.pageSize,
        initialLoadSize = pagingConfig.pageSize,
        enablePlaceholders = pagingConfig.enablePlaceholders,
        prefetchDistance = pagingConfig.prefetchDistance,
        jumpThreshold = pagingConfig.jumpThreshold,
        maxSize = pagingConfig.maxSize
    )

    return SearcherPaginator(
        pagingConfig = validPagingConfig,
        pagingSourceFactory = { SearcherPagingSource(searcher, transformer) }
    )
}

/**
 * A cold Flow of [PagingData], emits new instances of [PagingData] once they become invalidated.
 */
public val <T : Any> Paginator<T>.flow: Flow<PagingData<T>> get() = pager.flow

/**
 * A LiveData of [PagingData], which mirrors the stream provided by [flow], but exposes it as a LiveData.
 */
public val <T : Any> Paginator<T>.liveData: LiveData<PagingData<T>> get() = pager.liveData
