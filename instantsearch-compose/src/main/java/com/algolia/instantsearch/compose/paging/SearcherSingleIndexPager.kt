package com.algolia.instantsearch.compose.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.flow.Flow

public class SearcherSingleIndexPager<T : Any>(
    searcher: SearcherSingleIndex,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch) -> List<T>
) {

    public val flow: Flow<PagingData<T>> = Pager(pagingConfig) {
        SearcherSingleIndexPagingSource(
            searcher = searcher,
            transformer = transformer
        )
    }.flow

    private var reset: (() -> Unit)? = null

    public fun reset() {
        reset?.invoke()
    }

    public fun onReset(reset: () -> Unit) {
        this.reset = reset
    }
}
