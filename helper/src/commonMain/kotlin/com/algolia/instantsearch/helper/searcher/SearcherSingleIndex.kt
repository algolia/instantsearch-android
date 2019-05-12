package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.MainDispatcher
import com.algolia.instantsearch.core.searcher.Searcher
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
    val index: Index,
    val query: Query = Query(),
    val filterState: FilterState = FilterState(),
    val requestOptions: RequestOptions? = null,
    val disjunctiveFacetingEnabled: Boolean = true
) : Searcher, CoroutineScope {

    internal val sequencer = Sequencer()

    override val coroutineContext = SupervisorJob()

    public val onResponseChanged = mutableListOf<(ResponseSearch) -> Unit>()
    public val errorListeners = mutableListOf<(Throwable) -> Unit>()

    public var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
        if (newValue != null) {
            onResponseChanged.forEach { it(newValue) }
        }
    }
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorListeners.forEach { it(throwable) }
    }

    init {
        filterState.onChange += { search() }
    }

    override fun search() {
        val disjunctiveAttributes = filterState.getFacetGroups()
            .filter { it.key is FilterGroupID.Or }
            .flatMap { group -> group.value.map { it.attribute } }

        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
        val job = launch(MainDispatcher + exceptionHandler) {
            response = withContext(Dispatchers.Default) {
                if (disjunctiveAttributes.isEmpty() || !disjunctiveFacetingEnabled) {
                    index.search(query, requestOptions)
                } else {
                    index.searchDisjunctiveFacets(query, disjunctiveAttributes, filterState.getFilters())
                }
            }
        }
        sequencer.addOperation(job)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}

