package searcher

import com.algolia.search.client.ClientSearch
import com.algolia.search.client.RequestOptions
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearches
import kotlinx.coroutines.*


class SearcherMultiQuery(
    val client: ClientSearch,
    val indexQueries: List<IndexQuery>,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearches>? = null

    val listeners = mutableListOf<(ResponseSearches) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            val response = client.multipleQueries(indexQueries, requestOptions = requestOptions)

            listeners.forEach { it(response) }
            completed?.complete(response)
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}
