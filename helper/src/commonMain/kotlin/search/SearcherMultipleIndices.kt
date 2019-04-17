package search

import MainDispatcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


//TODO: FilterState
public class SearcherMultipleIndices(
    val client: ClientSearch,
    val indexQueries: List<IndexQuery>,
    val strategy: MultipleQueriesStrategy? = null,
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    private val sequencer = Sequencer()
    override val coroutineContext = Job()

    public val responseListeners = mutableListOf<(ResponseSearches) -> Unit>()

    public var response by Delegates.observable<ResponseSearches?>(null) { _, _, newValue ->
        if (newValue != null) {
            responseListeners.forEach { it(newValue) }
        }
    }

    override fun search(): Job {
        val job = launch {
            val responseSearches = client.multipleQueries(indexQueries, strategy, requestOptions)

            withContext(MainDispatcher) { response = responseSearches }
        }
        sequencer.addOperation(job)
        job.invokeOnCompletion { sequencer.operationCompleted(job) }
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}
