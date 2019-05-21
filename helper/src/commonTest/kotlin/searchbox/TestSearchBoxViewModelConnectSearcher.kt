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
    private fun setup(vararg expectedSearches: String): Triple<MockSearchBoxView, SearchBoxViewModel, MockSearcher> {
        val view = MockSearchBoxView()
        val viewModel = SearchBoxViewModel()
        val mockSearcher = MockSearcher(*expectedSearches)
        viewModel.connectView(view)
        return Triple(view, viewModel, mockSearcher)
    }

    @Test
    fun connectSearchAsYouType() {
        val (view, viewModel, mockSearcher) = setup("4", "42")
        viewModel.connectSearcher(mockSearcher)
        view.text = "4"
        view.text = "42"
    }

    @Test
    fun connectSearchOnSubmit() {
        val (view, viewModel, mockSearcher) = setup("42")
        viewModel.connectSearcher(mockSearcher, false)
        view.text = "4"
        view.text = "42"
        view.submit()
    }

    @Test
    fun connectUsesQueryHook() {
        val (view, viewModel, mockSearcher) = setup("42")
        var firstHookCall = true
        viewModel.connectSearcher(mockSearcher, queryHook = {
            if (firstHookCall) {
                firstHookCall = false
                false
            } else {
                true
            }
        })
        view.text = "4"
        view.text = "42"
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