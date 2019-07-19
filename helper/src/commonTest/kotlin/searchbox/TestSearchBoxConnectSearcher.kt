package searchbox

import blocking
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import searcher.MockSearcher
import shouldEqual
import kotlin.test.Test


class TestSearchBoxConnectSearcher {

    private val text = "text"

    @Test
    fun searchAsYouType() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val debouncer = Debouncer(100)

        viewModel.connectSearcher(searcher, searchAsYouType = true, debouncer = debouncer)
        viewModel.query.value = text
        blocking { debouncer.job!!.join() }
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun onEventSend() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()

        viewModel.connectSearcher(searcher, searchAsYouType = false)
        viewModel.eventSubmit.send(text)
        blocking { searcher.job!!.join() }
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual text
    }

    @Test
    fun debounce() {
        val searcher = MockSearcher()
        val viewModel = SearchBoxViewModel()
        val debouncer = Debouncer(100)

        viewModel.connectSearcher(searcher, debouncer = debouncer)
        viewModel.query.value = "a"
        viewModel.query.value = "ab"
        viewModel.query.value = "abc"
        blocking { debouncer.job!!.join() }
        searcher.searchCount shouldEqual 1
        searcher.string shouldEqual "abc"
    }
}