package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroupConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import filter.MutableFilterState
import filter.toFilterGroups
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


class SearcherSingleIndex(
    val index: Index,
    val query: Query = Query(),
    val filterState: MutableFilterState = MutableFilterState(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    override val coroutineContext = Job()

    private val sequencer = Sequencer()

    internal var completed: CompletableDeferred<ResponseSearch>? = null

    val responseListeners = mutableListOf<(ResponseSearch) -> Unit>()
    val errorListeners = mutableListOf<(Exception) -> Unit>()

    var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
        if (newValue != null) {
            responseListeners.forEach { it(newValue) }
        }
    }

    override fun search() {
        completed = CompletableDeferred()
        query.filters = FilterGroupConverter.SQL(filterState.get().toFilterGroups())
        launch {
            sequencer.addOperation(this)
            try {
                val responseSearch = index.search(query, requestOptions)

                withContext(MainDispatcher) {
                    response = responseSearch
                }
                completed?.complete(responseSearch)
            } catch (exception: Exception) {
                withContext(MainDispatcher) {
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

