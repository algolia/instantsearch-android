package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.asList
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.client.Index
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


public class SearcherWidget(
    public val searcher: SearcherSingleIndex,
    public val filterState: FilterState = FilterState(),
    public val debouncer: Debouncer = Debouncer(0)
) : ConnectionImplWidget() {

    public constructor(
        index: Index,
        query: Query = Query(),
        requestOptions: RequestOptions? = null,
        isDisjunctiveFacetingEnabled: Boolean = true,
        coroutineScope: CoroutineScope = SearcherScope(),
        filters: Map<FilterGroupID, Set<Filter>> = mapOf(),
        debouncingInMillis: Long = 0
    ) : this(
        SearcherSingleIndex(index, query, requestOptions, isDisjunctiveFacetingEnabled, coroutineScope),
        FilterState(filters),
        Debouncer(debouncingInMillis)
    )

    override val connections = searcher
        .connectFilterState(filterState, debouncer)
        .asList()
        .connect()

    public fun search(): Job {
        return searcher.searchAsync()
    }

    public fun cancel() {
        searcher.cancel()
    }
}