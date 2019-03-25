package searcher

import com.algolia.search.client.Index
import com.algolia.search.filter.FilterBuilder
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.properties.Delegates


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

    var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
        if (newValue != null) {
            launch {
                withContext(Dispatchers.Main) {
                    responseListeners.forEach { it(newValue) }
                }
            }
        }
    }

    override fun search() {
        completed = CompletableDeferred()
        query.filters = filterBuilder.build()
        launch {
            sequencer.addOperation(this)
            try {
                val responseSearch = index.search(query, requestOptions) //TODO ask Q: Why the temp var?
                response = responseSearch
                completed?.complete(responseSearch)
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    errorListeners.forEach { it(exception) }
                }
            }
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}

