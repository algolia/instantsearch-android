package searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import kotlinx.coroutines.*


class MockSearcher : Searcher<Unit> {

    var job: Job? = null
    var string: String? = null
    var searchCount: Int = 0

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<Unit?>(null)
    override val coroutineScope: CoroutineScope = SearcherScope()

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