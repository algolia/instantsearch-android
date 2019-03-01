package searcher

import com.algolia.search.client.Index
import com.algolia.search.client.RequestOptions
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacetValue
import com.algolia.search.model.search.Query
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


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
            val response = index.searchForFacetValue(attribute, facetQuery, query, maxFacetHits, requestOptions)

            println(response)
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