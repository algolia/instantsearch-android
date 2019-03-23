package searcher

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


class SearcherMultipleIndices(
    val client: ClientSearch,
    val indexQueries: List<IndexQuery>,
    val strategy: MultipleQueriesStrategy? = null,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearches>? = null

    val responseListeners = mutableListOf<(ResponseSearches) -> Unit>()
    val errorListeners = mutableListOf<(Exception) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            try {
                val response = client.multipleQueries(indexQueries, strategy, requestOptions)

                responseListeners.forEach { it(response) }
                completed?.complete(response)
            } catch (exception: Exception) {
                errorListeners.forEach { it(exception) }
            }
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}
