package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.model.response.ResultSearch
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 *  Extracts queries from queries sources, performs search request and dispatches the results to the corresponding receivers.
 */
@ExperimentalInstantSearch
public abstract class MultiSearcher : Searcher<ResponseMultiSearch> {

    /**
     * Client to perform operations on indices.
     */
    internal abstract val client: ClientSearch

    /**
     * Adds a search component to the multi-searcher.
     */
    internal abstract fun addSearcher(component: MultiSearchComponent<IndexedQuery, ResultSearch>)
}

/**
 * Creates an instance of [MultiSearcher].
 *
 * @param client search client instance
 * @param strategy multi-queries search strategy
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 */
@ExperimentalInstantSearch
public fun MultiSearcher(
    client: ClientSearch,
    strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope()
): MultiSearcher = DefaultMultiSearcher(
    client = client,
    strategy = strategy,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
)
