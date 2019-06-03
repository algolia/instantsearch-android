package searchbox

import blocking
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import kotlinx.coroutines.*
import shouldEqual
import kotlin.test.Test


class TestSearchBoxConnectSearcher {

    private val text = "text"

    private class MockSearcher: Searcher {

        var job: Job? = null
        var string: String? = null
        var searchCount: Int = 0

        override val coroutineScope: CoroutineScope = SearcherScope()
        override val dispatcher: CoroutineDispatcher = Dispatchers.Default

        override fun setQuery(text: String?) {
            string = text
        }

        override fun search(): Job {
            searchCount++
            val job = coroutineScope.launch {  }

            this.job = job
            return job
        }

        override fun cancel() = Unit
    }

    @Test
    fun searchAsYouType() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val debouncer = Debouncer(100)

        viewModel.connectSearcher(searcher, searchAsYouType = true, debouncer = debouncer)
        viewModel.item = text
        blocking { debouncer.job!!.join() }
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun onQuerySubmitted() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()

        viewModel.connectSearcher(searcher, searchAsYouType = false)
        viewModel.item = text
        viewModel.submitQuery()
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun debounce() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val debouncer = Debouncer(100)

        viewModel.connectSearcher(searcher, debouncer = debouncer)
        viewModel.item = "a"
        viewModel.item = "ab"
        viewModel.item = "abc"
        blocking { debouncer.job!!.join() }
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual "abc"
    }
}