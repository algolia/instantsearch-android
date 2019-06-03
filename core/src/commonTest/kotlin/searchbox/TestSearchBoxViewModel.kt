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

        viewModel.onItemChanged += { expected = it }
        viewModel.item = value
        expected shouldEqual value
    }

    @Test
    fun submitQueryShouldCallOnQuerySubmitted() {
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val value = "hello"

        viewModel.onQuerySubmitted += { expected = it }
        viewModel.item = value
        viewModel.submitQuery()
        expected shouldEqual value
    }
}