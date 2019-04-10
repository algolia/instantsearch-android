package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.FacetQuery
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer


class SearcherForFacets(
    val index: Index,
    val attribute: Attribute,
    var facetQuery: FacetQuery = FacetQuery(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearchForFacets>? = null

    val responseListeners = mutableListOf<(ResponseSearchForFacets) -> Unit>()
    val errorListeners = mutableListOf<(Exception) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            try {
                val response = index.searchForFacets(attribute, facetQuery, requestOptions)

                withContext(MainDispatcher) {
                    responseListeners.forEach { it(response) }
                }
                completed?.complete(response)
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