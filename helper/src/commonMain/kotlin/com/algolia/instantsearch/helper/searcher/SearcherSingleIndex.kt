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
    public val requestOptions: RequestOptions? = RequestOptions(),
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher {

    internal val sequencer = Sequencer()

    public val onResponseChanged = mutableListOf<(ResponseSearch) -> Unit>()
    public val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    public override val onLoadingChanged = mutableListOf<(Boolean) -> Unit>()

    public override var loading: Boolean by Delegates.observable(false) { _, _, newValue ->
        onLoadingChanged.forEach { it(newValue) }
    }

    public var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
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

    internal var disjunctive: (() -> Pair<List<Attribute>, Set<Filter>>) = { listOf<Attribute>() to setOf() }

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun search(): Job {
        val (disjunctiveAttributes, filters) = disjunctive.invoke()

        loading = true
        val job = coroutineScope.launch(dispatcher + exceptionHandler) {
            response = withContext(Dispatchers.Default) {
                if (disjunctiveAttributes.isEmpty() || !isDisjunctiveFacetingEnabled) {
                    index.search(query, requestOptions)
                } else {
                    index.searchDisjunctiveFacets(query, disjunctiveAttributes, filters)
                }
            }
            loading = false
        }
        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}

