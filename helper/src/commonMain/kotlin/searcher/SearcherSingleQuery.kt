package searcher

import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import kotlinx.coroutines.*


class SearcherSingleQuery(
    val index: Index,
    val query: Query
) : Searcher, CoroutineScope {

    private val sequencer = Sequencer()

    override val coroutineContext = Job()

    val listeners = mutableListOf<(ResponseSearch) -> Unit>()

    var completed: CompletableDeferred<ResponseSearch>? = null

    override fun search() {
        completed = CompletableDeferred()
        launch {
            sequencer.addOperation(this)
            val hits = index.search(query)

            listeners.forEach { it(hits) }
            completed?.complete(hits)
            sequencer.operationCompleted(this)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}
