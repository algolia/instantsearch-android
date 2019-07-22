package selectable.list

import com.algolia.instantsearch.core.Callback
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

        var list: List<SelectableItem<String>> = listOf()

        override var onSelection: Callback<String>? = null

        override fun setItems(items: List<SelectableItem<String>>) {
            list = items
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.selections.value = selections
        viewModel.connectView(view)
        view.list shouldEqual listOf(string to true)
    }

    @Test
    fun onItemsChangedShouldCallSetItem() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(emptyList(), SelectionMode.Multiple)

        viewModel.connectView(view)
        viewModel.items.value.shouldBeEmpty()
        viewModel.items.value = items
        view.list shouldEqual listOf(string to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetItems() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.connectView(view)
        viewModel.selections.value = selections
        view.list shouldEqual listOf(string to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockFilterListViewFacet()
        val viewModel = SelectableListViewModel<String, String>(items, SelectionMode.Multiple)

        viewModel.eventSelection.subscribe { viewModel.selections.value = it }
        viewModel.connectView(view)
        view.onSelection.shouldNotBeNull()
        view.onSelection!!(string)
        view.list shouldEqual listOf(string to true)
    }
}