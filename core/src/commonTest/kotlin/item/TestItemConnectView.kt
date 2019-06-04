package item

import com.algolia.instantsearch.core.item.ItemView
import com.algolia.instantsearch.core.item.ItemViewModel
import com.algolia.instantsearch.core.item.connectView
import shouldEqual
import kotlin.test.Test


class TestItemConnectView {

    private val input = 0
    private val output = "0"
    private val presenter: (Int) -> String = { it.toString() }

    private class MockItemView : ItemView<String> {

        var string: String? = null

        override fun setItem(item: String) {
            string = item
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val viewModel = ItemViewModel(input)
        val view = MockItemView()

        viewModel.connectView(view, presenter)
        view.string shouldEqual output
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val viewModel = ItemViewModel(input)
        val view = MockItemView()
        val expected = 1

        viewModel.connectView(view, presenter)
        viewModel.item = expected
        view.string shouldEqual "1"
    }
}