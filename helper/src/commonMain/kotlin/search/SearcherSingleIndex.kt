package search

import MainDispatcher
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import filter.FilterState
import filter.toFilterGroups
import kotlinx.coroutines.*
import searcher.Searcher
import searcher.Sequencer
import kotlin.properties.Delegates


public class SearcherSingleIndex(
    val index: Index,
    val query: Query = Query(),
    val filterState: FilterState = FilterState(),
    val requestOptions: RequestOptions? = null
) : Searcher, CoroutineScope {

    private val sequencer = Sequencer()
    override val coroutineContext = Job()

    public val onResponseChange = mutableListOf<(ResponseSearch) -> Unit>()

    public var response by Delegates.observable<ResponseSearch?>(null) { _, _, newValue ->
        if (newValue != null) {
            onResponseChange.forEach { it(newValue) }
        }
    }

    override fun search(): Job {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
        val job = launch(MainDispatcher) {
            val responseSearch = withContext(Dispatchers.Default) { index.search(query, requestOptions) }

            response = responseSearch
        }
        sequencer.addOperation(job)
        return job
    }

    override fun cancel() {
        sequencer.cancelAll()
        coroutineContext.cancelChildren()
    }
}

