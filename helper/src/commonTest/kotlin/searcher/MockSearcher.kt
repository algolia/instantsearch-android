package searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import kotlinx.coroutines.*


class MockSearcher : Searcher {

    var job: Job? = null
    var string: String? = null
    var searchCount: Int = 0

    override var loading: Boolean = false
    override val coroutineScope: CoroutineScope = SearcherScope()
    override val dispatcher: CoroutineDispatcher = Dispatchers.Default
    override val onLoadingChanged: MutableList<(Boolean) -> Unit> = mutableListOf()

    override fun setQuery(text: String?) {
        string = text
    }

    override fun search(): Job {
        searchCount++
        val job = coroutineScope.launch { }

        this.job = job
        return job
    }

    override fun cancel() = Unit
}