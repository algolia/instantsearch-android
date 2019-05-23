package searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import shouldEqual
import kotlin.test.Test


class TestSearchBoxViewModel  {

    @Test
    fun setQueryShouldCallOnQueryChanged() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.onQueryChanged += { expected = it }
        viewModel.query = value
        expected shouldEqual value
    }

    @Test
    fun submitQueryShouldCallOnQuerySubmitted() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.onQuerySubmitted += { expected = it }
        viewModel.query = value
        viewModel.submitQuery()
        expected shouldEqual value
    }
}