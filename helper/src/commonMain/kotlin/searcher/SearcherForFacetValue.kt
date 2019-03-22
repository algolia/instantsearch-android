package searcher

import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacetValue
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


class SearcherForFacetValue(
    val index: Index,
    val attribute: Attribute,
    var facetQuery: String? = null,
    val query: Query? = null,
    var maxFacetHits: Int? = null,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearchForFacetValue>? = null

    val listeners = mutableListOf<(ResponseSearchForFacetValue) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            val response = index.searchForFacetValues(attribute, facetQuery, query, requestOptions)

            listeners.forEach { it(response) }
            completed?.complete(response)
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        coroutineContext.cancelChildren()
        sequencer.cancelAll()
    }
}