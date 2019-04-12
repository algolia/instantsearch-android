package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.FilterGroupConverter
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.FacetQuery
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import filter.MutableFilterState
import filter.toFilterGroups
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


public class SearcherForFacets(
    public val index: Index,
    public val attribute: Attribute,
    public var facetQuery: FacetQuery = FacetQuery(query = Query()),
    public val filterState: MutableFilterState = MutableFilterState(),
    public val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    internal var completable: CompletableDeferred<ResponseSearchForFacets>? = null
    private val sequencer = Sequencer()
    override val coroutineContext = Job()

    public val responseListeners = mutableListOf<(ResponseSearchForFacets) -> Unit>()
    public val errorListeners = mutableListOf<(Exception) -> Unit>()

    public var response by Delegates.observable<ResponseSearchForFacets?>(null) { _, _, newValue ->
        if (newValue != null) {
            responseListeners.forEach { it(newValue) }
        }
    }

    override fun search() {
        completable = CompletableDeferred()
        facetQuery.query.filters = FilterGroupConverter.SQL(filterState.get().toFilterGroups())
        launch {
            sequencer.addOperation(this)
            try {
                val responseSearchForFacets = index.searchForFacets(attribute, facetQuery, requestOptions)

                withContext(MainDispatcher) {
                    response = responseSearchForFacets
                }
                completable?.complete(responseSearchForFacets)
            } catch (exception: Exception) {
                withContext(MainDispatcher) {
                    errorListeners.forEach { it(exception) }
                }
            }
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        coroutineContext.cancelChildren()
        sequencer.cancelAll()
    }
}