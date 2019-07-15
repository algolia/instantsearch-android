package searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import shouldEqual
import kotlin.test.Test


class TestSearchBoxViewModel {

    @Test
    fun setQueryShouldCallSubscription() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.query.subscribe { expected = it }
        viewModel.query.set(value)
        expected shouldEqual value
    }

    @Test
    fun sendEventShouldCallSubscription() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.event.subscribe { expected = it }
        viewModel.event.send(value)
        expected shouldEqual value
    }
}