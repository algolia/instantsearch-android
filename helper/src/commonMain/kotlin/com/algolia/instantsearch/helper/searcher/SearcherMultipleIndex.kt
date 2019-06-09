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
    public val queries: List<IndexQuery>,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearches> {

    internal val sequencer = Sequencer()

    override val dispatcher: CoroutineDispatcher = defaultDispatcher

    public override val onResponseChanged = mutableListOf<(ResponseSearches) -> Unit>()
    public override val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    override val onLoadingChanged: MutableList<(Boolean) -> Unit> = mutableListOf()

    override var loading by Delegates.observable(false) { _, _, newValue ->
        onLoadingChanged.forEach { it(newValue) }
    }

    public override var response by Delegates.observable<ResponseSearches?>(null) { _, _, newValue ->
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

    override fun setQuery(text: String?) {
        queries.forEach { it.query.query = text }
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            withContext(Dispatchers.Default) { search() }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearches {
        withContext(dispatcher) { loading = true }
        val response = client.multipleQueries(queries, strategy, requestOptions)
        withContext(dispatcher) {
            this@SearcherMultipleIndex.response = response
            loading = false
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}