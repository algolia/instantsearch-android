package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


public class SearcherForFacets(
    public var index: Index,
    public val attribute: Attribute,
    public val query: Query = Query(),
    public var facetQuery: String? = null,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher {

    internal val sequencer = Sequencer()

    public val onResponseChanged = mutableListOf<(ResponseSearchForFacets) -> Unit>()
    public val onErrorChanged = mutableListOf<(Throwable) -> Unit>()
    public override val onLoadingChanged = mutableListOf<(Boolean) -> Unit>()

    public override var loading: Boolean by Delegates.observable(false) { _, _, newValue ->
        onLoadingChanged.forEach { it(newValue) }
    }

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
        facetQuery = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) { search() }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search() {
        loading = true
        response = withContext(Dispatchers.Default) {
            index.searchForFacets(attribute, facetQuery, query, requestOptions)
        }
        loading = false
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}