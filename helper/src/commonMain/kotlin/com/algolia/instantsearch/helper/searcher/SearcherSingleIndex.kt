package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


public class SearcherSingleIndex(
    public var index: Index,
    public val query: Query = Query(),
    public val requestOptions: RequestOptions? = null,
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearch> {

    internal val sequencer = Sequencer()

    override val dispatcher: CoroutineDispatcher = defaultDispatcher

    public override val onResponseChanged = mutableListOf<(ResponseSearch) -> Unit>()
    public override val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    public override val onLoadingChanged = mutableListOf<(Boolean) -> Unit>()

    public override var loading by Delegates.observable(false) { _, _, newValue ->
        onLoadingChanged.forEach { it(newValue) }
    }

    public override var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
        if (newValue != null) {
            onResponseChanged.forEach { it(newValue) }
        }
    }
    public override var error by Delegates.observable<Throwable?>(null) { _, _, newValue ->
        if (newValue != null) {
            onErrorChanged.forEach { it(newValue) }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error = throwable
    }

    internal var disjunctive: (() -> Pair<List<Attribute>, Set<Filter>>) = { listOf<Attribute>() to setOf() }

    internal var hierarchicalAttributes: List<Attribute> = listOf()
    internal var hierarchicalFilters: List<Filter.Facet> = listOf()

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            withContext(Dispatchers.Default) { search() }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch {
        val (disjunctiveAttributes, disjunctiveFilters) = disjunctive()

        withContext(dispatcher) { loading = true }
        val response = if (hierarchicalFilters.isNotEmpty()) {
            index.searchHierarchical(
                query,
                disjunctiveAttributes,
                disjunctiveFilters,
                hierarchicalAttributes,
                hierarchicalFilters
            )
        } else if (disjunctiveAttributes.isEmpty() || !isDisjunctiveFacetingEnabled) {
            index.search(query, requestOptions)
        } else {
            index.searchDisjunctiveFacets(query, disjunctiveAttributes, disjunctiveFilters)
        }
        withContext(dispatcher) {
            this@SearcherSingleIndex.response = response
            loading = false
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}