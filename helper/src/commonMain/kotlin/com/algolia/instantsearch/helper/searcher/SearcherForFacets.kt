package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


public class SearcherForFacets(
    var index: Index,
    val attribute: Attribute,
    val filterState: FilterState = FilterState(),
    val query: Query = Query(),
    var facetQuery: String? = null,
    val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : Searcher {

    internal val sequencer = Sequencer()

    public val onResponseChanged = mutableListOf<(ResponseSearchForFacets) -> Unit>()
    public val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    public val onQueryChanged = mutableListOf<(String?) -> Unit>()

    public var response by Delegates.observable<ResponseSearchForFacets?>(null) { _, _, newValue ->
        if (newValue != null) {
            onResponseChanged.forEach { it(newValue) }
        }
    }
    public var error by Delegates.observable<Throwable?>(null) { _, _, newValue ->
        if (newValue != null) {
            onErrorChanged.forEach { it(newValue) }
        }
    }
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error = throwable
    }

    override fun setQuery(text: String?) {
        if (text != facetQuery) {
            facetQuery = text
            onQueryChanged.forEach { it(text) }
        }
    }

    override fun search(): Job {
        val job = coroutineScope.launch(dispatcher + exceptionHandler) {
            response = withContext(Dispatchers.Default) {
                index.searchForFacets(attribute, facetQuery, query, requestOptions)
            }
        }
        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}