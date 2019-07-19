package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherMultipleIndex(
    public val client: ClientSearch,
    public val queries: List<IndexQuery>,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearches> {

    internal val sequencer = Sequencer()

    override val dispatcher: CoroutineDispatcher = defaultDispatcher
    override val isLoading = ObservableItem(false)
    override val error = ObservableItem<Throwable?>(null)
    override val response = ObservableItem<ResponseSearches?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
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
        withContext(dispatcher) { isLoading.value = true }
        val response = client.multipleQueries(queries, strategy, requestOptions)
        withContext(dispatcher) {
            this@SearcherMultipleIndex.response.value = response
            isLoading.value = false
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}