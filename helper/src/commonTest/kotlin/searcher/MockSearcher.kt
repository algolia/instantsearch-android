package searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import kotlinx.coroutines.*


class MockSearcher : Searcher<Unit> {

    var job: Job? = null
    var string: String? = null
    var searchCount: Int = 0

    override var loading: Boolean = false
    override val coroutineScope: CoroutineScope = SearcherScope()
    override val dispatcher: CoroutineDispatcher = Dispatchers.Default
    override val onLoadingChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    override var response: Unit? = null
    override var error: Throwable? = null
    override val onResponseChanged: MutableList<(Unit) -> Unit> = mutableListOf()
    override val onErrorChanged: MutableList<(Throwable) -> Unit> = mutableListOf()

    override fun setQuery(text: String?) {
        string = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch { search() }.also { job = it }
    }

    override suspend fun search() {
        searchCount++
    }

    override fun cancel() = Unit
}