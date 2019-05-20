package searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searchbox.SearchBoxView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.Searcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import shouldEqual
import kotlin.test.Test

class TestSearchBoxViewModelConnectSearcher {
    @Test
    fun connectSearchAsYouType() {
        val view = MockSearchBoxView()
        val viewModel = SearchBoxViewModel()
        var round = 0
        val mockSearcher = MockSearcher("4", "42")
        viewModel.connectView(view)
        viewModel.connectSearcher(mockSearcher)
        view.text = "4"
        view.text = "42"
    }

    @Test
    fun connectSearchOnSubmit() {
        val view = MockSearchBoxView()
        val viewModel = SearchBoxViewModel()
        val mockSearcher = MockSearcher("42")
        viewModel.connectView(view)
        viewModel.connectSearcher(mockSearcher, false)
        view.text = "4"
        view.text = "42"
        view.submit()
    }

    private class MockSearcher(vararg val expectedSearches: String) : Searcher {
        private var query: String? = null
        private var searchId = 0

        override val dispatcher = Dispatchers.Main

        override fun setQuery(text: String?) {
            query = text
        }

        override fun search(): Job {
            query shouldEqual expectedSearches[searchId++]
            return Job()
        }

        override fun cancel() = Unit
    }

    private class MockSearchBoxView : SearchBoxView {
        var text: String = ""
            set(value) {
                field = value
                changeListener?.invoke(value)
            }

        var changeListener: ((String?) -> Unit)? = null
        var submitListener: ((String?) -> Unit)? = null

        override fun setTextChangeListener(listener: (String?) -> Unit) {
            changeListener = listener
        }

        override fun setTextSubmitListener(listener: (String?) -> Unit) {
            submitListener = listener
        }

        fun submit() {
            submitListener?.invoke(text)
        }
    }
}