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
        viewModel.query.value = value
        expected shouldEqual value
    }

    @Test
    fun sendEventShouldCallSubscription() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.eventSubmit.subscribe { expected = it }
        viewModel.eventSubmit.send(value)
        expected shouldEqual value
    }
}
