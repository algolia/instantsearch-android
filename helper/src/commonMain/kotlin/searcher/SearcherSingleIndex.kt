package searcher

import com.algolia.search.client.Index
import com.algolia.search.filter.FilterBuilder
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


class SearcherSingleIndex(
    val index: Index,
    val query: Query,
    val filterBuilder: FilterBuilder = FilterBuilder(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearch>? = null

    val responseListeners = mutableListOf<(ResponseSearch) -> Unit>()
    val errorListeners = mutableListOf<(Exception) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        query.filters = filterBuilder.build()
        launch {
            sequencer.addOperation(this)
            try {
                val response = index.search(query, requestOptions)
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

