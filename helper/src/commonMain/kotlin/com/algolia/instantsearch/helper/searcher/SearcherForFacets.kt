package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherForFacets(
    public var index: Index,
    public val attribute: Attribute,
    public val query: Query = Query(),
    public var facetQuery: String? = null,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearchForFacets> {

    internal val sequencer = Sequencer()

    override val dispatcher: CoroutineDispatcher = defaultDispatcher
    override val isLoading =  ObservableItem(false)
    override val error = ObservableItem<Throwable?>(null)
    override val response = ObservableItem<ResponseSearchForFacets?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.set(throwable)
    }

    override fun setQuery(text: String?) {
        facetQuery = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            isLoading.set(true)
            withContext(Dispatchers.Default) { search() }
            isLoading.set(false)
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearchForFacets {
        withContext(dispatcher) { isLoading.set(true) }
        val response =  index.searchForFacets(attribute, facetQuery, query, requestOptions)
        withContext(dispatcher) {
            this@SearcherForFacets.response.set(response)
            isLoading.set(false)
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}