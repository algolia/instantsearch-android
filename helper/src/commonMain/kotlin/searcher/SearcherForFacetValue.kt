package searcher

import com.algolia.search.client.Index
import com.algolia.search.filter.FilterBuilder
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacetValue
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


class SearcherForFacetValue(
    val index: Index,
    val attribute: Attribute,
    var facetQuery: String? = null,
    val query: Query? = null,
    val filterBuilder: FilterBuilder = FilterBuilder(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearchForFacetValue>? = null

    val responseListeners = mutableListOf<(ResponseSearchForFacetValue) -> Unit>()
    val errorListeners = mutableListOf<(Exception) -> Unit>()

    var response by Delegates.observable<ResponseSearchForFacetValue?>(null) { _, _, newValue ->
        if (newValue != null) {
            responseListeners.forEach { it(newValue) }
        }
    }

    override fun search() {
        completed = CompletableDeferred()
        query?.filters = filterBuilder.build()
        launch {
            sequencer.addOperation(this)
            try {
                val responseSearch = index.searchForFacetValues(attribute, facetQuery, query, requestOptions)

                response = responseSearch
                completed?.complete(responseSearch)
            } catch (exception: Exception) {
                errorListeners.forEach { it(exception) }
            }
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        coroutineContext.cancelChildren()
        sequencer.cancelAll()
    }
}