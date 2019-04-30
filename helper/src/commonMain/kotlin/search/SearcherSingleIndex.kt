package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import filter.FilterGroupID
import filter.FilterState
import filter.toFilterGroups
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


public class SearcherSingleIndex(
    val index: Index,
    val query: Query = Query(),
    val filterState: FilterState = FilterState(),
    val requestOptions: RequestOptions? = null
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
        val facets = filterState.getFacetGroups()
        val filters = facets.flatMap { it.value }
        val disjunctiveAttributes = facets
            .filter { it.key is FilterGroupID.Or }
            .flatMap { group -> group.value.map { it.attribute } }

        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
        val job = launch(MainDispatcher + exceptionHandler) {
            response = withContext(Dispatchers.Default) {
                if (disjunctiveAttributes.isEmpty()) {
                    index.search(query, requestOptions)
                } else {
                    index.searchDisjunctiveFacets(query, disjunctiveAttributes, filters)
                }
            }
        }
        sequencer.addOperation(job)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}

