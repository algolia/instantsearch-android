package searcher

import com.algolia.search.client.Index
import com.algolia.search.client.RequestOptions
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import kotlinx.coroutines.*


class SearcherSingleIndex(
    val index: Index,
    val query: Query,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearch>? = null

    val listeners = mutableListOf<(ResponseSearch) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            val response = index.search(query, requestOptions)

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
