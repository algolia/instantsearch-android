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
    fun connectShouldSetItem() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()

        viewModel.query.set(text)
        viewModel.connectView(view)
        view.text shouldEqual text
    }

    @Test
    fun onQueryChangedShouldCallSubscription() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()
        var expected: String? = null

        viewModel.query.subscribe { expected = it }
        viewModel.connectView(view)
        view.onQueryChanged.shouldNotBeNull()
        view.onQueryChanged!!(text)
        viewModel.query.get() shouldEqual text
        expected shouldEqual text
    }

    @Test
    fun onQuerySubmittedShouldCallSubscription() {
        val viewModel = SearchBoxViewModel()
        val view = MockView()
        var expected: String? = null

        viewModel.event.subscribe { expected = it }
        viewModel.connectView(view)
        view.onQuerySubmitted.shouldNotBeNull()
        view.onQuerySubmitted!!(text)
        viewModel.query.get() shouldEqual text
        expected shouldEqual text
    }
}