package searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSearchBoxConnectView {

    private val text = "text"

    private class MockView : SearchBoxView {

        var text: String? = null
        var queryChanged: String? = null
        var querySubmitted: String? = null

        override fun setItem(item: String?) {
            text = item
        }

        override var onQueryChanged: ((String?) -> Unit)? = {
            queryChanged = it
        }
        override var onQuerySubmitted: ((String?) -> Unit)? = {
            querySubmitted = it
        }
    }

    @Test
    fun connectViewShouldSetQuery() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()

        viewModel.item = text
        viewModel.connectView(view)
        view.text shouldEqual text
    }

    @Test
    fun onQueryChangedShouldCallOnQueryChanged() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()
        var expected: String? = null

        viewModel.onItemChanged += { expected = it }
        viewModel.connectView(view)
        view.onQueryChanged.shouldNotBeNull()
        view.onQueryChanged!!(text)
        viewModel.item shouldEqual text
        expected shouldEqual text
    }

    @Test
    fun onQuerySubmittedShouldCallOnQuerySubmitted() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()
        var expected: String? = null

        viewModel.onQuerySubmitted += { expected = it }
        viewModel.connectView(view)
        view.onQuerySubmitted.shouldNotBeNull()
        view.onQuerySubmitted!!(text)
        viewModel.item shouldEqual text
        expected shouldEqual text
    }
}