package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


public class SearcherMultipleIndex(
    public val client: ClientSearch,
    public val strategy: MultipleQueriesStrategy,
    public val queries: List<IndexQuery>,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher {

    internal val sequencer = Sequencer()

    public val onResponseChanged = mutableListOf<(ResponseSearches) -> Unit>()
    public val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    override val onLoadingChanged: MutableList<(Boolean) -> Unit> = mutableListOf()

    override var loading: Boolean = false

    public var response by Delegates.observable<ResponseSearches?>(null) { _, _, newValue ->
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
        queries.forEach { it.query.query = text }
    }

    override fun search(): Job {
        loading = true
        val job = coroutineScope.launch(dispatcher + exceptionHandler) {
            response = client.multipleQueries(queries, strategy, requestOptions)
            loading = false
        }

        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}