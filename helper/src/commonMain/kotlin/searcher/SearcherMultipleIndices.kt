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
    val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
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
            val response = client.multipleQueries(indexQueries, strategy, requestOptions)

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
