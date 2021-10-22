package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearcherImpl
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.model.response.ResultSearch
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * Extracts queries from queries sources, performs search request and dispatches the results to the corresponding receivers.
 */
public interface MultiSearcher : Searcher<ResponseMultiSearch>

/**
 * Creates an instance of [MultiSearcher].
 */
public fun MultiSearcher(
    client: ClientSearch,
    strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope()
): MultiSearcher = MultiSearcherImpl(
    client = client,
    strategy = strategy,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
)
