package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.FacetQuery
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import filter.FilterState
import filter.toFilterGroups
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


public class SearcherForFacets(
    val index: Index,
    val attribute: Attribute,
    var facetQuery: FacetQuery = FacetQuery(query = Query()),
    val filterState: FilterState = FilterState(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    private val sequencer = Sequencer()
    override val coroutineContext = Job()

    public val responseListeners = mutableListOf<(ResponseSearchForFacets) -> Unit>()

    public var response by Delegates.observable<ResponseSearchForFacets?>(null) { _, _, newValue ->
        if (newValue != null) {
            responseListeners.forEach { it(newValue) }
        }
    }

    override fun search(): Job {
        facetQuery.query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
        val job = launch {
            val responseSearchForFacets = index.searchForFacets(attribute, facetQuery, requestOptions)

            withContext(MainDispatcher) { response = responseSearchForFacets }
        }
        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        coroutineContext.cancelChildren()
        sequencer.cancelAll()
    }
}