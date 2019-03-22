package searcher

import com.algolia.search.client.Index
import com.algolia.search.client.RequestOptions
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import errors.InstantSearchException
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.response.readText
import kotlinx.coroutines.*


class SearcherSingleIndex(
    val index: Index,
    val query: Query,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearch>? = null

    val responseListeners = mutableListOf<(ResponseSearch) -> Unit>()
    val errorListeners = mutableListOf<(InstantSearchException) -> Unit>()

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            try {
                val response = index.search(query, requestOptions)
                responseListeners.forEach { it(response) }
                completed?.complete(response)
                sequencer.operationCompleted(this)
            } catch (error: Exception) {
                val errorMessage = if (error is BadResponseStatusException) error.response.readText() else null
                errorListeners.forEach { it(InstantSearchException(errorMessage, error)) }
            }
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}

