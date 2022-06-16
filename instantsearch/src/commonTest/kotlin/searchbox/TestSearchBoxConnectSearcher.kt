package searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectSearcher
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import searcher.MockSearcher
import shouldEqual

class TestSearchBoxConnectSearcher {

    private val text = "text"
    private val debouncer = Debouncer(100)

    @Test
    fun searchAsYouType() = runTest {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectSearcher(searcher, SearchMode.AsYouType, debouncer)

        connection.connect()
        viewModel.query.value = text
        debouncer.job!!.join()
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun onEventSend() = runTest {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectSearcher(searcher, SearchMode.OnSubmit, debouncer)

        connection.connect()
        viewModel.eventSubmit.send(text)
        searcher.job!!.join()
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun debounce() = runTest {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectSearcher(searcher, SearchMode.AsYouType, debouncer)

        connection.connect()
        viewModel.query.value = "a"
        viewModel.query.value = "ab"
        viewModel.query.value = "abc"
        debouncer.job!!.join()
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual "abc"
    }
}
