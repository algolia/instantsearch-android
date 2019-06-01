package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


public class SearcherSingleIndex(
    public var index: Index,
    public val filterState: FilterState = FilterState(),
    public val query: Query = Query(),
    public val requestOptions: RequestOptions? = RequestOptions(),
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher {

    internal val sequencer = Sequencer()

    public val onResponseChanged = mutableListOf<(ResponseSearch) -> Unit>()
    public val onErrorChanged = mutableListOf<(Throwable) -> Unit>()

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

    private fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    init {
        updateFilters()
        filterState.onChanged += {
            updateFilters()
            search()
        }
    }

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun search(): Job {
        val disjunctiveAttributes = filterState.getFacetGroups()
            .filter { it.key is FilterGroupID.Or }
            .flatMap { group -> group.value.map { it.attribute } }

        val job = coroutineScope.launch(dispatcher + exceptionHandler) {
            response = withContext(Dispatchers.Default) {
                if (disjunctiveAttributes.isEmpty() || !isDisjunctiveFacetingEnabled) {
                    index.search(query, requestOptions)
                } else {
                    index.searchDisjunctiveFacets(query, disjunctiveAttributes, filterState.getFilters())
                }
            }
        }
        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}

