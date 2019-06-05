package selectable.list

import com.algolia.instantsearch.core.selectable.list.*
import shouldBeEmpty
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSelectableListConnectView {

    private val string = "string"
    private val items = listOf(string)
    private val selections = setOf(string)

    private class MockFilterListViewFacet : SelectableListView<String> {

        var items: List<SelectableItem<String>> = listOf()

        override var onClick: ((String) -> Unit)? = null

        override fun setItem(item: List<SelectableItem<String>>) {
            items = item
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.selections = selections
        viewModel.connectView(view)
        view.items shouldEqual listOf(string to true)
    }

    @Test
    fun onItemsChangedShouldCallSetItem() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(emptyList(), SelectionMode.Multiple)

        viewModel.connectView(view)
        viewModel.item.shouldBeEmpty()
        viewModel.item = items
        view.items shouldEqual listOf(string to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetItems() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(string to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(string)
        view.items shouldEqual listOf(string to true)
    }
}